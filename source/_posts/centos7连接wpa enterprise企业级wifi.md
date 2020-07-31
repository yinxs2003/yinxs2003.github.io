---
title: python3下配置spark环境
date: 2020-07-10 12:00:30
toc: true
tags: Linux
categories: 
- 技术
---



```shell
$ network-manager.nmcli connection edit type 802-11-wireless
nmcli> goto 802-11-wireless
nmcli 802-11-wireless> set ssid <your_ssid>
nmcli 802-11-wireless> back
nmcli> goto 802-11-wireless-security
nmcli 802-11-wireless-security> set key-mgmt wpa-eap
nmcli 802-11-wireless-security> set auth-alg open
nmcli 802-11-wireless-security> back
nmcli> goto 802-1x
nmcli 802-1x> set eap peap
nmcli 802-1x> set identity <your_identity>
nmcli 802-1x> set password <your_password>
nmcli 802-1x> set phase2-auth mschapv2
nmcli 802-1x> back
nmcli> verify
nmcli> save
Saving the connection with 'autoconnect=yes'. That might result in an immediate activation of the connection.
Do you still want to save? (yes/no) [yes] yes
Connection 'wifi' (20e7bab0-6780-45a7-b650-eafb28e7912a) successfully saved.
```

 保存后就自动连上了