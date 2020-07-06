---
title: Fast文件上传
date: 2016-06-03 10:40:30
tags: Linux
---
# FastDFS系统
## 安装FastDFS5.08
``` bash
http://www.cnblogs.com/Yin-BloodMage/p/5480608.html
```
## FastDFS系统JavaClient客户端
### 单元测试的例子
JavaClient用法单元测试程序，里面最重要的一点是可以new一个新的StorageClient实例，该实例可以直接调用上传下载文件方法。
网上那些写了一堆的来创建StoreageClient实例的做法不能说是错的，但在多线程环境下有时候会出些莫名其妙的问题，而且很丑陋。这里只列举上传方法，其他删除下载和上传类似。
``` java
public class Meta {
    private String groupName;
    private String upPath;

    public Meta() {
    }

    public Meta(String groupName, String upPath) {
        this.groupName = groupName;
        this.upPath = upPath;
    }
    // getter setter
}
```
``` java
/**
 * 单元测试
 */
public class FDFSTest {
    private FDFSServiceImpl fdfsService = null;
    private Recorder recorder = Recorder.getInstance();
    private Logger logger = Logger.getLogger(FDFSTest.class);
    private Meta meta = new Meta("group1", "M00/01/B4/rBsMQFdQES2ACE0yAABFapaYBmE511.png");

    @Before
    public void before() throws IOException, MyException {
        ConfigManager configManager = ConfigManager.getInstance();
        fdfsService = new FDFSServiceImpl();
        ClientGlobal.init(configManager.getConf_filename());
    }

    // 上传测试开始
    @Test
    public void testUpload() throws IOException, MyException {
        String localPath = "conf/test.png";
        NameValuePair[] nvp = new NameValuePair[] { new NameValuePair("name", "test.png"),
                new NameValuePair("size", "18K") };
        String[] fileIds = new String[0];
        StorageClient client;
        Meta aMeta;
        try {
            client = new StorageClient();
            fileIds = client.upload_file(localPath, null, nvp);
            aMeta = new Meta(fileIds[0], fileIds[1]);

            // 将上传的文件路径记录到文件
            //recorder.recordUploadedFileList(aMeta);
            logger.info(aMeta);
        } catch (Exception e) {
            logger.error(null, e);
        }
        Assert.assertNotEquals("fileIds shouldn't be 0", 0, fileIds.length);

    }
}
```
### 并发上传
使用的是fixedThreadPool来实现3000并发上传单张图片，并发代码如下
``` java
public class UploadThreadTest {
    public static void main(String[] args) {
        startToUpload();
    }

    private static void startToUpload() {
        ConfigManager configManager = ConfigManager.getInstance();
        //线程池固定大小为16，不能使用newCachedPool会有问题        
        ExecutorService exec = Executors.newFixedThreadPool(16);
        UploadThread command = new UploadThread();
        //getMaxConcurrentNum是获取最大并发数
        for (int i = 0; i < configManager.getMaxConcurrentNum(); i++) {
            exec.execute(command);
        }
        exec.shutdown();
    }
}
```
``` java
public class UploadThread implements Runnable {
    private FDFSServiceImpl fdfsService;
    private NameValuePair[] nvp = new NameValuePair[] { new NameValuePair("name", "test.png"),
            new NameValuePair("size", "18K") };

    public UploadThread() {
        fdfsService = new FDFSServiceImpl();
    }

    @Override
    public void run() {
        fdfsService.upload(nvp);
    }
}
```
``` java
    /** 
     * @param nvp       上传文件的属性
     * @param localPath 本地文件路径
     * @return 上传后文件数据元
     */
    public Meta upload(NameValuePair[] nvp, String localPath) {
        String[] fileIds;
        Meta meta = null;
        StorageClient client;
        try {

            client = getStorageClient();
            fileIds = client.upload_file(localPath, null, nvp);
            meta = new Meta(fileIds[0], fileIds[1]);

            // 将上传的文件路径记录到文件
            recorder.recordUploadedFileList(meta);
            logger.info(meta);
        } catch (Exception e) {
            logger.error(null, e);
        }
        return meta;
    }
```
### 断点续上传
大文件的上传,需要使用append_file方法，上传时候需要分片，注意最后一片是小于你的预定义buff长度的
``` java
public void tryAppend(Meta meta) {
        logger.info("try to append file: " + appendFilePath);
        // 本地文件长度
        long length = new File(appendFilePath).length();
        long skipSize = 0;

        FileInfo fileInfo = readFileInfo(meta);
        long uploadedSize;
        // 上传了文件的大小
        GetUploadedFileSize getUploadedFileSize = new GetUploadedFileSize(fileInfo).invoke();

        // 判断是否已经上传，若已经append完毕,return
        if (getUploadedFileSize.hasUploaded())
            return;

        // 未上传完毕,则获取已经上传了的大小
        uploadedSize = getUploadedFileSize.getUploadedSize();
        if (CheckIfHasFinishedUpload(length, skipSize, uploadedSize))
            return;

        //执行上传
        doTryAppend(meta, appendFilePath, length);
    }
```
``` java
    private boolean CheckIfHasFinishedUpload(long length, long skipSize, long uploadedSize) {
        logger.warn("remote file length " + uploadedSize + "local file name " + appendFilePath
                + " ------ local file length " + length);
        if (uploadedSize == length) {
            logger.info("---> This file have been uploaded successfully,remote file size hasUploaded: " + skipSize);
            return true;
        } else if (uploadedSize > length) {
            logger.error("uploaded error!remote file bigger than local file !!");
            return true;
        }
        return false;
    }
```

``` java
    /**
     * @param meta
     *            数据元
     * @param appendFilePath
     *            本地文件路径
     * @param totalFileSize
     *            要上传的文件的大小
     */
    public void doTryAppend(Meta meta, String appendFilePath, long totalFileSize) {
        logger.info("appendFilePath: " + appendFilePath);

        try (InputStream is = new FileInputStream(appendFilePath)) {
            append(meta, totalFileSize, is);
            recorder.recordUploadedFileList(meta);
        } catch (IOException e) {
            logger.error(null, e);
        }
        logger.info("文件上传完成");
    }
```
``` java
    /**
     * @param meta
     *            数据元
     * @param totalFileSize
     *            文件总大小
     * @param is
     *            上传使用的输入流
     * @throws IOException
     */
    private void append(Meta meta, long totalFileSize, InputStream is) throws IOException {
        // 已经上传的大小
        long haveUploadedFileSize = 0L;
        // 获取已经上传的大小
        haveUploadedFileSize = getHaveUploadedFileSize(meta, haveUploadedFileSize);
        skipHaveUploadSize(haveUploadedFileSize, is);
        printUploadInfo(totalFileSize, haveUploadedFileSize);
        appendBrokenUpload(meta, totalFileSize, is, haveUploadedFileSize);
    }
```
自己起的名字，表示从这里开始执行分片上传
``` java
    private void appendBrokenUpload(Meta meta, long totalFileSize, InputStream is, long haveUploadedFileSize)
            throws IOException {
        // 上传时分割
        byte[] buff = new byte[section_size];
        while (haveUploadedFileSize < totalFileSize) {
            // 每次读多少，最后一次文件长度为文件剩余长度而不是buff长度
            int readCount = getReadCount(totalFileSize, haveUploadedFileSize, buff, is);
            // 3次尝试上传都失败则不上传该文件，并记录已上传的大小，下次上传可以skip已上传大小后，再上传
            uploadByFragment(meta, buff, readCount);
            haveUploadedFileSize += readCount;
            printUploadInfo(totalFileSize, haveUploadedFileSize);
        }
    }
```
``` java
    private void printUploadInfo(long totalFileSize, long haveUploadedFileSize) {
        logger.info("upload progress : " + haveUploadedFileSize + "/" + totalFileSize);
    }
```
分片上传
``` java
    /**
     * 分片上传
     *
     * @param meta
     *            数据元
     * @param buff
     *            分片大小
     * @param readCount
     *            每次上传的大小
     */
    private void uploadByFragment(Meta meta, byte[] buff, int readCount) {
        int count = 0;
        while (count < 3) {
            int uploadedSize = -1;
            try {
                if (checkIfLastFragment(buff, readCount)) {
                    uploadedSize = uploadLastFragment(meta, buff, readCount);
                } else {
                    uploadedSize = storageClient.append_file(meta.getGroupName(), meta.getUpPath(), buff);
                }
            } catch (Exception e) {
                logger.error(null, e);
            }
            count++;
            //上传结果result为零，说明本次上传失败或者是整个文件上传完毕，跳出
            if (uploadedSize == 0) {
                break;
            }
        }
    }
```
上传最后一片内容
``` java
    /**
     * @param meta
     *            数据元
     * @param buff
     *            分片的载体
     * @param readCount
     *            实际上传了多少个字节
     * @return 返回上传的实际大小
     * @throws IOException
     * @throws MyException
     */
    private int uploadLastFragment(Meta meta, byte[] buff, int readCount) throws IOException, MyException {
        int uploadedSize;
        byte[] newBuffer = new byte[readCount];
        System.arraycopy(buff, 0, newBuffer, 0, readCount);
        uploadedSize = storageClient.append_file(meta.getGroupName(), meta.getUpPath(), newBuffer);
        return uploadedSize;
    }
```
断点续上传，是先获取已上传文件大小，然后用InputStream类的skip方法把已经上传的大小skip过去，来达到断点上传的效果
``` java
    private void skipHaveUploadSize(long skipSize, InputStream is) throws IOException {
        // noinspection ResultOfMethodCallIgnored
        is.skip(skipSize);
    }
```
``` java
    private long getHaveUploadedFileSize(Meta meta, long skipSize) {
        FileInfo fileInfo = readFileInfo(meta);
        if (fileInfo != null) {
            skipSize = fileInfo.getFileSize();
        }
        return skipSize;
    }
```
### 大文件分片下载
还有就是大文件下载，当文件比较大的时候（大于500M）,会报OutOfMemoryError，解决方法是对大文件分片下载
``` java
    /**
     * 分片下载防止内存溢出(OutOfMemoryError)
     *
     * @param meta     数据元
     * @param pathName 本地文件路径
     */
    public void downloadByFragment(Meta meta, String pathName) {
        long remoteLength = readFileInfo(meta).getFileSize();
        long hasDownSize = 0;
        byte[] buff;
        FileOutputStream fos = null;
        try {
            long fragmentSize = SECTION_SIZE;
            do {
                // 检测是否为最后一片，是就只下载剩余的文件长度，而不是整个SECTION_SIZE的大小
                if (checkIfLastFragment(remoteLength, hasDownSize)) {
                    fragmentSize = getLeftFragmentSize(remoteLength, hasDownSize);
                }
                buff = getStorageClient().download_file(meta.getGroupName(), meta.getUpPath(), hasDownSize,
                        fragmentSize);
                printDownloadInfo(remoteLength, hasDownSize);
                if (buff == null) {
                    System.out.println("download finished");
                    break;
                }
                fos = writeToDisk(pathName, buff);
                hasDownSize += SECTION_SIZE;
            } while (true);
        } catch (MyException | IOException e) {
            logger.error(null, e);
        } finally {
            closeResource(fos);
        }
    }
```



检测是否下载最后一片
``` java
 private boolean checkIfLastFragment(long remoteLength, long offset) {
        return offset + SECTION_SIZE > remoteLength;
    }
```