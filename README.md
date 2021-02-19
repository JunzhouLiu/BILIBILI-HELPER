<div align="center">
<h1 align="center">
BILIBILI-HELPER
</h1>

[![GitHub stars](https://img.shields.io/github/stars/JunzhouLiu/BILIBILI-HELPER?style=flat-square)](https://github.com/JunzhouLiu/BILIBILI-HELPER/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/JunzhouLiu/BILIBILI-HELPER?style=flat-square)](https://github.com/JunzhouLiu/BILIBILI-HELPER/network)
[![GitHub issues](https://img.shields.io/github/issues/JunzhouLiu/BILIBILI-HELPER?style=flat-square)](https://github.com/JunzhouLiu/BILIBILI-HELPER/issues)
[![GitHub license](https://img.shields.io/github/license/JunzhouLiu/BILIBILI-HELPER?style=flat-square)](https://github.com/JunzhouLiu/BILIBILI-HELPER/blob/main/LICENSE) 
[![GitHub All Releases](https://img.shields.io/github/downloads/JunzhouLiu/BILIBILI-HELPER/total?style=flat-square)](https://github.com/JunzhouLiu/BILIBILI-HELPER/releases)
[![Docker Pulls](https://img.shields.io/docker/pulls/superng6/bilibili-helper?style=flat-square)](https://hub.docker.com/r/superng6/bilibili-helper)
[![GitHub contributors](https://img.shields.io/github/contributors/JunzhouLiu/BILIBILI-HELPER?style=flat-square)](https://github.com/JunzhouLiu/BILIBILI-HELPER/graphs/contributors)
![GitHub release (latest SemVer)](https://img.shields.io/github/v/release/JunzhouLiu/BILIBILI-HELPER?style=flat-square)
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2FJunzhouLiu%2FBILIBILI-HELPER.svg?type=flat-square)](https://app.fossa.com/projects/git%2Bgithub.com%2FJunzhouLiu%2FBILIBILI-HELPER?ref=badge_shield)

</div>

## 工具简介

这是一个利用 Linux Crontab , GitHub Action 等方式实现哔哩哔哩（Bilibili）每日任务投币，点赞，分享视频，直播签到，银瓜子兑换硬币，漫画每日签到，简单配置即可每日轻松获取 65 经验值，快来和我一起成为 Lv6 吧\~\~\~\~

**如果觉得好用，顺手点个 Star 吧 ❤**

**仓库地址：[JunzhouLiu/BILIBILI-HELPER][1]**

**请不要滥用相关API，让我们一起爱护B站 ❤**

## 功能列表

* [x] 每天上午 9 点 10 分自动开始任务。*【运行时间可自定义】*
* [x] 哔哩哔哩漫画每日自动签到，自动阅读1章节 。
* [x] 每日自动从热门视频中随机观看 1 个视频，分享一个视频。
* [x] 每日从热门视频中选取 5 个进行智能投币 *【如果投币不能获得经验了，则不会投币】*
* [x] 投币支持下次一定啦，可自定义每日投币数量。*【如果检测到你已经投过币了，则不会投币】*
* [x] 大会员月底使用快到期的 B币券，给自己充电，一点也不会浪费哦，默认开启。*【已支持给指定UP充电】*
* [x] 大会员月初 1 号自动领取每月 5 张 B 币券和福利。
* [x] 每日哔哩哔哩直播自动签到，领取签到奖励。*【直播你可以不看，但是奖励咱们一定要领】*
* [x] 通过server酱推送执行结果到微信。
* [x] Linux用户支持自定义配置了。
* [x] 投币策略更新可配置投币喜好。*【可配置优先给关注的up投币】*
* [x] 自动送出即将过期的礼物。 *【默认开启，未更新到新版本的用户默认关闭】*
	  
[点此查看更新日志][2]

[点击快速开始使用][3]

[点击快速查看自定义功能配置][4]

# 目录

- [目录][5]
  - [使用说明][6]
	- [一、Actions 方式][7]
	- [二、使用 Docker][8]
	- [三、使用 Linux Crontab 方式][9]
	- [自定义功能配置][10]
  - [微信订阅通知][11]
	- [订阅执行结果][12]
  - [Telegram订阅通知](#Telegram订阅通知)
      - [Telegram订阅执行结果](#Telegram订阅执行结果)
  - [更新和帮助][13]
	- [使用 Github Actions 自动同步源仓库代码][14]
	- [手动拉取最新代码][15]
	- [使用Pull APP［推荐］][16]
	- [常见问题解答][17]
  - [免责声明][18]
  - [API 参考列表][19]
  - [基于本项目的衍生项目][20]
  - [致谢][21]
  - [License][22]
  - [Stargazers over time][23]

## 使用说明

### 一、Actions 方式

1. **Fork 本项目**
2. **获取 Bilibili Cookies**
3. 浏览器打开并登录 [bilibili 网站][24]
4. 按 F12 打开 「开发者工具」 找到 应用程序/Application -\> 存储 -\> Cookies
5. 找到 `bili_jct` `SESSDATA` `DEDEUSERID` 三项，并复制值，创建对应的 GitHub Secrets。

![图示][image-1]

3. **点击项目 Settings -\> Secrets -\> New Secrets 添加以下 3 个 Secrets，其中server酱微信推送的sckey可参阅[微信订阅通知][25]**

| Name          | Value               |
| ------------- | ------------------- |
| DEDEUSERID    | 从 Cookie 中获取    |
| SESSDATA      | 从 Cookie 中获取    |
| BILI\_JCT     | 从 Cookie 中获取    |
| SERVERPUSHKEY | server酱推送的sckey |


![图示][image-2]

1. **开启 Actions 并触发每日自动执行**

**Github Actions 默认处于关闭状态，还大家请手动开启 Actions ，执行一次工作流，验证是否可以正常工作。**

![图示][image-3]

**Fork 仓库后，GitHub 默认不自动执行 Actions 任务，请修改 `.github/trigger.json` 文件,将 `trigger` 的值改为 `1`，这样每天就会自动执行定时任务了。**

```patch
{
- "trigger": 0
+ "trigger": 1
}
```

**
由于[issues/313](https://github.com/JunzhouLiu/BILIBILI-HELPER/issues/313)中反馈接收到了B站的警告，所以跳过每日任务选项默认开启(也就是默认不执行每日任务)，如需关闭，请将`src/main/resources/config.json`中的`skipDailyTask`值改为`false`
,**

```patch
{
  "numberOfCoins": 5,
  "reserveCoins": 50,
  "selectLike": 0,
  "monthEndAutoCharge": true,
  "giveGift": true,
  "upLive": "0",
  "chargeForLove": "0",
  "devicePlatform": "ios",
  "coinAddPriority": 1,
- "skipDailyTask": true,
+ "skipDailyTask": false,
  "userAgent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0 Safari/605.1.15"
}
```

如果需要修改每日任务执行的时间，请修改 `.github/workflows/auto_task_bilili.yml`，在第 12 行左右位置找到下如下配置。

```yml
  schedule:
    - cron: '30 10 * * *'
    # cron表达式，Actions时区是国际时间，国际时间10点的时候，国内时间是18点。
    # 示例： 每天晚上22点30执行 '30 14 * * *'
```

本工具的 Actions 自动构建配置了缓存，平均运行时间在 20s 左右。

*如果收到了 GitHub Action 的错误邮件，请检查 Cookies 是不是失效了，用户修改密码、踢除设备下线，会导致 `BILI_JCT` 和 `DEDEUSERID` 失效*

**请各位使用 Actions 时务必遵守Github条款。不要滥用Actions服务。**

**Please be sure to abide by the Github terms when using Actions. Do not abuse the Actions service.**

**查看 Actions 运行日志**

[Actions 运行日志详细查看教程][26]

[日志示例][27]


### 二、使用 Docker

请自行参阅 [Issues/75#issuecomment-731705657][28] 和[基于本项目的衍生项目][29] 。


### 三、使用 Linux Crontab 方式

1. 在linux shell环境执行以下命令，并按照提示输入SESSDATA，DEDEUSERID，BILI\_JCT，SCKEY四个参数
```
wget https://raw.githubusercontent.com/JunzhouLiu/BILIBILI-HELPER/main/setup.sh && chmod +x ./setup.sh && sudo ./setup.sh
```

**ps：注意，如果使用自定义配置，请将`config.json`和jar包放置在同一目录(使用setup.sh安装则需要将`config.json`放置到`{HOME}/BILIBILI-HELPER`)，`v1.2.2`之后的版本`release`中都会携带一份`config.json`。**

2. 除此之外，也可以通过点击 [BILIBILI-HELPER/release][30]，下载已发布的版本，解压后将jar包手动上传到Linux服务器，使用crontab完成定时执行，如果使用`crontab`请记得`source /etc/profile`和`source ~/.bashrc`,建议直接使用仓库提供的[`start.sh`][31]脚本,注意修改脚本的jar包路径和cookies参数。


**crontab命令示例**

`30 10 * * * sh /home/start.sh` 

| args              | 说明               |
| ----------------- | ------------------ |
| 30 10 \* \* \*    | `crontab` 定时时间 |
| sh /home/start.sh | `start.sh`的路径   |

```shell
#!/bin/bash
source /etc/profile 
source ~/.bashrc 
source ~/.zshrc #其他终端请自行引入环境变量
echo $PATH
java -jar /home/BILIBILI-HELPER.jar DEDEUSERID SESSDATA BILI_JCT SCKEY >> /var/log/bilibili-help.log
# 注意将jar包路径替换为实际路径。将参数修改该你自己的参数，cookies中含有等特殊字符需要转义。
```


**命令示例：**

```shell
# *如果Cookies参数中包含特殊字符，例如`%`请使用`\`转义*,如果不执行可在命令前增加 source /etc/profile
# m h  dom mon dow   command
30 10 * * * java -jar /home/BILIBILI-HELP.jar DEDEUSERID SESSDATA BILI_JCT >/var/log/cron.log &
```

### 自定义功能配置

**Actions任务配置文件位于 `src/main/resources/config.json`**

配置文件示例：

```json
{
  "numberOfCoins": 5,
  "reserveCoins": 50,
  "selectLike": 0,
  "monthEndAutoCharge": true,
  "giveGift": true,
  "upLive": "0",
  "chargeForLove": "0",
  "devicePlatform": "ios",
  "coinAddPriority": 1,
  "skipDailyTask": true,
  "userAgent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0 Safari/605.1.15"
}
```

**Windows/Linux 用户使用jar包时，`release`包中会包含一份`config.json`配置文件，只需将其和`BILIBILI-HELP.jar`放在同一目录即可，执行时优先加载外部配置文件**


配置文件参数示意

| Key                | Value             | 说明                                                                    |
| ------------------ | ----------------- | ----------------------------------------------------------------------- |
| numberOfCoins      | [0,5]             | 每日投币数量,默认 5 ,为0时则不投币                                      |
| reserveCoins       | [0,4000]          | 预留的硬币数，当硬币余额小于这个值时，不会进行投币任务，默认值为50      |
| selectLike         | [0,1]             | 投币时是否点赞，默认 0, 0：否 1：是                                     |
| monthEndAutoCharge | [false,true]      | 年度大会员月底是否用 B币券给自己充电，默认 `true`，即充电对象是你本人。 |
| giveGift           | [false,true]      | 直播送出即将过期的礼物，默认开启，如需关闭请改为false                   |
| upLive             | [0,送礼up主的uid] | 直播送出即将过期的礼物，指定up主，为0时则随随机选取一个up主             |
| chargeForLove      | [0,充电对象的uid] | 给指定up主充电，值为0或者充电对象的uid，默认为0，即给自己充电。         |
| devicePlatform     | [ios,android]     | 手机端漫画签到时的平台，建议选择你设备的平台 ，默认 `ios`               |
| coinAddPriority    | [0,1]             | 0：优先给热榜视频投币，1：优先给关注的up投币                            |
| userAgent          | 浏览器UA          | 用户可根据部署平台配置，可根据userAgent参数列表自由选取                 |
| skipDailyTask      | [false,true]      | 是否跳过每日任务，默认`true`,如果关闭跳过每日任务，请改为`false`        |

\*\*tips:如果你没有上传过视频并开启充电计划，充电会失败，B币券会浪费。此时建议配置为给指定的up主充电。欢迎给即将秃头的我充电
uid：[14602398][32] \*\*

userAgent可选参数列表

| 平台      | 浏览器         | userAgent                                                                                                                           |
| --------- | -------------- | ----------------------------------------------------------------------------------------------------------------------------------- |
| Windows10 | EDGE(chromium) | Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36 Edg/86.0.622.69 |
| Windows10 | Chrome         | Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36                 |
| masOS     | safari         | Mozilla/5.0 (Macintosh; Intel Mac OS X 10\_15\_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0 Safari/605.1.15             |
| macOS     | Firefox        | Mozilla/5.0 (Macintosh; Intel Mac OS X 10.12; rv:65.0) Gecko/20100101 Firefox/65.0                                                  |
| macOS     | Chrome         | Mozilla/5.0 (Macintosh; Intel Mac OS X 10\_12\_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.75 Safari/537.36          |

*ps：如果尝试给关注的 up 投币十次后（保不准你关注的是年更up主），还没完成每日投币任务，则切换成热榜模式，给热榜视频投币*

*投币数量代码做了处理，如果本日投币不能获得经验了，则不会投币，每天只投能获得经验的硬币。假设你设置每日投币 3 个，早上 7 点你自己投了 2 个硬币，则十点半时，程序只会投 1 个）*


## 微信订阅通知

### 订阅执行结果

1. 前往 [sc.ftqq.com][33] 点击登入，创建账号（建议使用 GitHub 登录）。
2. 点击点[发送消息][34] ，生成一个 Key。将其增加到 Github Secrets 中，变量名为 `SERVERPUSHKEY`
3. [绑定微信账号][35] ，开启微信推送。
![图示][image-4]
4. 推送效果展示
![图示][image-5]

## Telegram订阅通知

### Telegram订阅执行结果

1.在Telegram中添加BotFather这个账号，然后依次发送/start /newbot 按照提示即可创建一个新的机器人。记下来给你生成的token。

2.搜索刚刚创建的机器人的名字，并给它发送一条消息。

*特别注意：需要先与机器人之间创建会话，机器人才能下发消息，否则机器人无法主动发送消息，切记！

3.在Telegram中搜索userinfobot，并给它发送一条消息，它会返回给你chatid。

4.在Github Secrets中删除SERVERPUSHKEY，添加TELEGRAMBOTTOKEN，TELEGRAMCHATID。



## 更新和帮助

### 使用 Github Actions 自动同步源仓库代码

参阅[Issue8 使用GitHub Actions任务自动同步源仓库代码][36]

### 手动拉取最新代码

参阅[Issues4 手动拉取最新代码][37]

### 使用Pull APP［推荐］

参阅 [Pull APP][38]

### 常见问题解答

请参阅[常见问题解答][39]

## 免责声明

1. 本工具不会记录你的任何敏感信息，也不会上传到任何服务器上。（例如用户的cookies数据，cookies数据均存在Actions Secrets中或者用户自己的设备上）
2. 本工具不会记录任何执行过程中来自b站的数据信息，也不会上传到任何服务器上。（例如av号，bv号，用户uid等）。
3. 本工具执行过程中产生的日志，仅会在使用者自行配置推送渠道后进行推送。日志中不包含任何用户敏感信息。
4. 如果有人修改了本项目（或者直接使用本项目）盈利恰饭，那和我肯定没关系，我开源的目的单纯是技术分享。
5. 如果你使用了第三方修改的，打包的本工具代码，那你可得注意了，指不定人就把你的数据上传到他自己的服务器了，这可和我没关系。（**网络安全教育普及任重而道远**）
6. 本工具源码仅在[JunzhouLiu/BILIBILI-HELPER][40]开源，其余的地方的代码均不是我提交的，可能是抄我的，借鉴我的，但绝对不是我发布的，出问题和我也没关系。 
7. 我开源本工具的代码仅仅是技术分享，没有任何丝毫的盈利赚钱目的，如果你非要给我打赏/充电，那我就是网络乞丐，咱们不构成任何雇佣，购买关系的交易。
8. 本项目不会增加类似于自动转发抽奖，秒杀，下载版权受限视频等侵犯UP主/B站权益的功能，开发这个应用的目的是单纯的技术分享。下游分支开发者/使用者也请不要滥用相关功能。
9. 本项目欢迎其他开发者参与贡献，基于本工具的二次开发，使用其他语言重写都没有什么问题，能在技术上给你带来帮助和收获就很好.
10. 本项目遵守[MIT License][41] ，请各位知悉。


## API 参考列表

- [SocialSisterYi/bilibili-API-collect][42]
- [happy888888/BiliExp][43]

## 基于本项目的衍生项目

- **基于本项目的docker封装项目：[SuperNG6/docker-bilibili-helper][44]**

- **基于本项目的docker镜像：[superng6/bilibili-helper][45]**
	 
- **基于本项目的runer项目：[KurenaiRyu/bilibili-helper-runer][46]**

- **基于本项目的k8s项目：[yangyang0507/k8s-bilibili-helper][47]**

## 致谢
感谢 JetBrains 对本项目的支持。

[![JetBrains][image-6]][48] 

## License
[![FOSSA Status][image-7]][49]

## Stargazers over time

[![Stargazers over time][image-8]][50]

[1]:	https://github.com/JunzhouLiu/BILIBILI-HELPER
[2]:	https://github.com/JunzhouLiu/BILIBILI-HELPER/releases
[3]:	#%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E
[4]:	#%E8%87%AA%E5%AE%9A%E4%B9%89%E5%8A%9F%E8%83%BD%E9%85%8D%E7%BD%AE
[5]:	#%E7%9B%AE%E5%BD%95
[6]:	#%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E
[7]:	#%E4%B8%80actions-%E6%96%B9%E5%BC%8F
[8]:	#%E4%BA%8C%E4%BD%BF%E7%94%A8-docker
[9]:	#%E4%B8%89%E4%BD%BF%E7%94%A8-linux-crontab-%E6%96%B9%E5%BC%8F
[10]:	#%E8%87%AA%E5%AE%9A%E4%B9%89%E5%8A%9F%E8%83%BD%E9%85%8D%E7%BD%AE
[11]:	#%E5%BE%AE%E4%BF%A1%E8%AE%A2%E9%98%85%E9%80%9A%E7%9F%A5
[12]:	#%E8%AE%A2%E9%98%85%E6%89%A7%E8%A1%8C%E7%BB%93%E6%9E%9C
[13]:	#%E6%9B%B4%E6%96%B0%E5%92%8C%E5%B8%AE%E5%8A%A9
[14]:	#%E4%BD%BF%E7%94%A8-github-actions-%E8%87%AA%E5%8A%A8%E5%90%8C%E6%AD%A5%E6%BA%90%E4%BB%93%E5%BA%93%E4%BB%A3%E7%A0%81
[15]:	#%E6%89%8B%E5%8A%A8%E6%8B%89%E5%8F%96%E6%9C%80%E6%96%B0%E4%BB%A3%E7%A0%81
[16]:	#%E4%BD%BF%E7%94%A8pull-app%E6%8E%A8%E8%8D%90
[17]:	#%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98%E8%A7%A3%E7%AD%94
[18]:	#%E5%85%8D%E8%B4%A3%E5%A3%B0%E6%98%8E
[19]:	#api-%E5%8F%82%E8%80%83%E5%88%97%E8%A1%A8
[20]:	#%E5%9F%BA%E4%BA%8E%E6%9C%AC%E9%A1%B9%E7%9B%AE%E7%9A%84%E8%A1%8D%E7%94%9F%E9%A1%B9%E7%9B%AE
[21]:	#%E8%87%B4%E8%B0%A2
[22]:	#license
[23]:	#stargazers-over-time
[24]:	https://www.bilibili.com/
[25]:	#%E5%BE%AE%E4%BF%A1%E8%AE%A2%E9%98%85%E9%80%9A%E7%9F%A5
[26]:	https://github.com/JunzhouLiu/BILIBILI-HELPER/issues/21
[27]:	https://github.com/JunzhouLiu/BILIBILI-HELPER/runs/1256484004?check_suite_focus=true#step:4:5069
[28]:	https://github.com/JunzhouLiu/BILIBILI-HELPER/issues/75#issuecomment-731705657
[29]:	#%E5%9F%BA%E4%BA%8E%E6%9C%AC%E9%A1%B9%E7%9B%AE%E7%9A%84%E8%A1%8D%E7%94%9F%E9%A1%B9%E7%9B%AE
[30]:	https://github.com/JunzhouLiu/BILIBILI-HELPER/releases/latest
[31]:	https://github.com/JunzhouLiu/BILIBILI-HELPER/blob/main/start.sh
[32]:	https://space.bilibili.com/14602398
[33]:	http://sc.ftqq.com/3.version
[34]:	http://sc.ftqq.com/?c=code
[35]:	http://sc.ftqq.com/?c=wechat&a=bind
[36]:	https://github.com/JunzhouLiu/BILIBILI-HELPER/issues/8
[37]:	https://github.com/JunzhouLiu/BILIBILI-HELPER/issues/4
[38]:	https://github.com/apps/pull
[39]:	https://github.com/JunzhouLiu/BILIBILI-HELPER/issues/4
[40]:	https://github.com/JunzhouLiu/BILIBILI-HELPER
[41]:	https://github.com/JunzhouLiu/BILIBILI-HELPER/blob/main/LICENSE
[42]:	https://github.com/SocialSisterYi/bilibili-API-collect
[43]:	https://github.com/happy888888/BiliExp
[44]:	https://github.com/SuperNG6/docker-bilibili-helper
[45]:	https://hub.docker.com/r/superng6/bilibili-helper
[46]:	https://github.com/KurenaiRyu/bilibili-helper-runer
[47]:	https://github.com/yangyang0507/k8s-bilibili-helper
[48]:	https://www.jetbrains.com/?from=BILIBILI-HELPER
[49]:	https://app.fossa.com/projects/git%2Bgithub.com%2FJunzhouLiu%2FBILIBILI-HELPER?ref=badge_large
[50]:	https://starchart.cc/JunzhouLiu/BILIBILI-HELPER

[image-1]:	docs/IMG/20201012001307.png
[image-2]:	docs/IMG/20201013210000.png
[image-3]:	docs/IMG/workflow_dispatch.png
[image-4]:	docs/IMG/serverpush.png
[image-5]:	docs/IMG/wechatMsgPush.png
[image-6]:	docs/IMG/jetbrains.svg
[image-7]:	https://app.fossa.com/api/projects/git%2Bgithub.com%2FJunzhouLiu%2FBILIBILI-HELPER.svg?type=large
[image-8]:	https://starchart.cc/JunzhouLiu/BILIBILI-HELPER.svg
