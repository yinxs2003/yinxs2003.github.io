import os


def insert_data(path, index, value):
    f = open(path, "r")
    contents = f.readlines()
    f.close()
    if len(contents) <= index:
        return
    line = contents[index]
    if not line.startswith(value):
        print('生成摘要')
        contents.insert(index, value)
        f = open(path, "w")
        contents = "".join(contents)
        f.write(contents)
        f.close()
    else:
        print('do nothing')


def list_dir(save_path):
    file_list = []
    for item in os.listdir(save_path):
        if item.endswith(".md"):
            file_list.append(item)
    return file_list


def generate_files():
    save_path = 'source/_posts'
    index = 5
    value = '\n文章内容\n<!--more-->\n'
    file_list = list_dir(save_path)
    for path in file_list:
        path1 = os.path.join(save_path, path)
        insert_data(path1, index, value)


if __name__ == '__main__':
    generate_files()

# insert_data(path, 20, '<!--more-->')
