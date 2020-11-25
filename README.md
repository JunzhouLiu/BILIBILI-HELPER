<div align="center">
<h1 align="center">
BILIBILI-HELPER
</h1>

[![GitHub stars](https://img.shields.io/github/stars/JunzhouLiu/BILIBILI-HELPER?style=flat-square)](https://github.com/JunzhouLiu/BILIBILI-HELPER/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/JunzhouLiu/BILIBILI-HELPER?style=flat-square)](https://github.com/JunzhouLiu/BILIBILI-HELPER/network)
[![GitHub issues](https://img.shields.io/github/issues/JunzhouLiu/BILIBILI-HELPER?style=flat-square)](https://github.com/JunzhouLiu/BILIBILI-HELPER/issues)
[![GitHub license](https://img.shields.io/github/license/JunzhouLiu/BILIBILI-HELPER?style=flat-square)](https://github.com/JunzhouLiu/BILIBILI-HELPER/blob/main/LICENSE) 
[![GitHub All Releases](https://img.shields.io/github/downloads/JunzhouLiu/BILIBILI-HELPER/total?style=flat-square)](https://github.com/JunzhouLiu/BILIBILI-HELPER/releases)
[![GitHub contributors](https://img.shields.io/github/contributors/JunzhouLiu/BILIBILI-HELPER?style=flat-square)](https://github.com/JunzhouLiu/BILIBILI-HELPER/graphs/contributors)
![GitHub release (latest SemVer)](https://img.shields.io/github/v/release/JunzhouLiu/BILIBILI-HELPER?style=flat-square)

</div>

# 工具简介

这是一个利用 Linux Crontab , GitHub Action 等方式实现哔哩哔哩（Bilibili）每日任务投币，点赞，分享视频，直播签到，银瓜子兑换硬币，漫画每日签到，简单配置即可每日轻松获取 65 经验值，快来和我一起成为 Lv6 吧~~~~

**如果觉得好用，顺手点个 Star 吧 ❤**

**仓库地址：[JunzhouLiu/BILIBILI-HELPER](https://github.com/JunzhouLiu/BILIBILI-HELPER)**

## 功能列表

**本项目不会增加类似于自动转发抽奖，秒杀，下载版权受限视频等侵犯UP主/B站权益的功能，开发这个应用的目的是单纯的技术分享。下游分支开发者/使用者也请不要滥用相关功能。**

**本项目欢迎其他开发者参与贡献，基于本工具的二次开发，使用其他语言重写都没有什么问题，能在技术上给你带来帮助和收获就很好**

**请不要滥用相关API，让我们一起爱护B站 ❤**

* [x] 每天上午 9 点 10 分自动开始任务。*【运行时间可自定义】*
* [x] 哔哩哔哩漫画每日自动签到 。
* [x] 每日自动从热门视频中随机观看 1 个视频，分享一个视频。
* [x] 每日从热门视频中选取 5 个进行智能投币 *【如果投币不能获得经验，默认不投币】*
* [x] 投币支持下次一定啦，可自定义每日投币数量。*【如果检测到你已经投过币了，则不会投币】*
* [x] 大会员月底使用快到期的 B币券，给自己充电，一点也不会浪费哦，默认开启。*【可配置】*
* [x] 大会员月初 1 号自动领取每月 5 张 B币券 和福利。
* [x] 每日哔哩哔哩直播自动签到，领取签到奖励。*【直播你可以不看，但是奖励咱们一定要领】*
* [x] 通过server酱推送执行结果到微信。
* [x] Linux用户支持自定义配置了。
* [x] 投币策略更新可配置投币喜好。*【可配置优先给关注的up投币】*
  
[点此查看更新日志](https://github.com/JunzhouLiu/BILIBILI-HELPER/blob/main/SECURITY.md)

[点击快速开始使用](#使用说明)

# 目录

- [工具简介](#工具简介)
  - [功能列表](#功能列表)
- [目录](#目录)
- [使用说明](#使用说明)
  - [一、Actions 方式](#一actions-方式)
    - [配置自定义功能](#配置自定义功能)
    - [查看运行日志](#查看运行日志)
  - [二、使用 Linux Crontab 方式](#二使用-linux-crontab-方式)
    - [步骤](#步骤)
    - [运行效果](#运行效果)
  - [三、使用 Windows10](#三使用-windows10)
    - [步骤](#步骤-1)
  - [四、使用 Docker](#四使用-docker)
- [微信订阅通知](#微信订阅通知)
  - [订阅执行结果](#订阅执行结果)
- [快速更新](#快速更新)
  - [使用 Github Actions 自动同步源仓库代码](#使用-github-actions-自动同步源仓库代码)
  - [手动拉取最新代码](#手动拉取最新代码)
- [常见问题解答](#常见问题解答)
- [免责声明](#免责声明)
- [致谢](#致谢)
- [API 参考列表](#api-参考列表)
- [基于本项目的衍生项目](#基于本项目的衍生项目)

# 使用说明

## 一、Actions 方式

1. **Fork 本项目**
2. **获取 Bilibili Cookies**
- 浏览器打开并登录 [bilibili 网站](https://www.bilibili.com/)
- 按 F12 打开 「开发者工具」 找到 应用程序/Application -> 存储 -> Cookies
- 找到 `bili_jct` `SESSDATA` `DEDEUSERID` 三项，并复制值，创建对应的 GitHub Secrets。

![图示](docs/IMG/20201012001307.png)

3. **点击项目 Settings -> Secrets -> New Secrets 添加以下 3 个 Secrets。**

| Name       | Value            |
| ---------- | ---------------- |
| DEDEUSERID | 从 Cookie 中获取 |
| SESSDATA   | 从 Cookie 中获取 |
| BILI_JCT   | 从 Cookie 中获取 |

![图示](docs/IMG/20201013210000.png)

4. **开启 Actions 并触发每日自动执行**

**Github Actions 默认处于关闭状态，还大家请手动开启 Actions ，执行一次工作流，验证是否可以正常工作。**

![图示](docs/IMG/workflow_dispatch.png)

**Fork 仓库后，GitHub 默认不自动执行 Actions 任务，请修改 `./github/trigger.json` 文件,将 `trigger` 的值改为 `1`，这样每天就会自动执行定时任务了。**

```patch
{
- "trigger": 0
+ "trigger": 1
}
```

如果需要修改每日任务执行的时间，请修改 `.github/workflows/auto_task_bilili.yml`，在第 12 行左右位置找到下如下配置。

```yml
  schedule:
    - cron: '30 10 * * *'
    # cron表达式，Actions时区是UTC时间，所以下午18点要往前推8个小时。
    # 示例： 每天晚上22点30执行 '30 14 * * *'
```

本工具的 Actions 自动构建配置了缓存，平均运行时间在 20s 左右。

*如果收到了 GitHub Action 的错误邮件，请检查 Cookies 是不是失效了，用户主动清除浏览器缓存，会导致 `BILI_JCT` 和 `DEDEUSERID` 失效*

**请各位使用 Actions 时务必遵守Github条款。不要滥用Actions服务。**

**Please be sure to abide by the Github terms when using Actions. Do not abuse the Actions service.**

### 配置自定义功能

**配置文件位于 `src/main/resources/config.json`**

参数示意

| Key                | Value         | 说明                                                                                                          |
| ------------------ | ------------- | ------------------------------------------------------------------------------------------------------------- |
| numberOfCoins      | [0,5]         | 每日投币数量,默认 5                                                                                           |
| selectLike         | [0,1]         | 投币时是否点赞，默认 0, 0：否 1：是                                                                           |
| ~~watchAndShare~~  | ~~[0,1]~~     | ~~观看时是否分享~~                                                                                            |
| monthEndAutoCharge | [false,true]  | 年度大会员月底是否用 B币券 给自己充电，默认 `true`                                                            |
| devicePlatform     | [ios,android] | 手机端漫画签到时的平台，建议选择你设备的平台 ，默认 `ios`                                                     |
| coinAddPriority    | [0,1]         | 0：优先给热榜视频投币，1：优先给关注的up投币                                                                  |
| userAgent          | 浏览器UA      | 用户可根据部署平台配置，可根据userAgent参数列表自由选取，如果触发了HTTP/1.1 412 Precondition Failed也请修改UA |

userAgent可选参数列表
| 平台      | 浏览器         | userAgent                                                                                                                           |
| --------- | -------------- | ----------------------------------------------------------------------------------------------------------------------------------- |
| Windows10 | EDGE(chromium) | Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36 Edg/86.0.622.69 |
| Windows10 | Chrome         | Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36                 |
| masOS     | safari         | Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0 Safari/605.1.15               |
| macOS     | Firefox        | Mozilla/5.0 (Macintosh; Intel Mac OS X 10.12; rv:65.0) Gecko/20100101 Firefox/65.0                                                  |
| macOS     | Chrome         | Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.75 Safari/537.36            |

*ps：如果尝试给关注的 up 投币十次后（保不准你关注的是年更up主），还没完成每日投币任务，则切换成热榜模式，给热榜视频投币*

*投币数量代码做了处理，如果本日投币不能获得经验了，则不会投币，每天只投能获得经验的硬币。假设你设置每日投币 3 个，早上 7 点你自己投了 2 个硬币，则十点半时，程序只会投 1 个）*

### 查看运行日志

*展开 `Build With Maven` 通过 `DEBUG` 标签快速过滤日志，查看运行状态*  

[Actions 运行日志详细查看教程](https://github.com/JunzhouLiu/BILIBILI-HELPER/issues/21)

[日志示例](https://github.com/JunzhouLiu/BILIBILI-HELPER/runs/1256484004?check_suite_focus=true#step:4:5069)

![图示](docs/IMG/debug1.png)
![图示](docs/IMG/debug2.png)

## 二、使用 Linux Crontab 方式

### 步骤

1. 在linux shell环境执行以下命令，并按照提示输入SESSDATA，DEDEUSERID，BILI_JCT，SCKEY四个参数
```
wget https://raw.githubusercontent.com/JunzhouLiu/BILIBILI-HELPER/main/setup.sh && chmod +x ./setup.sh && sudo ./setup.sh
```

**Linux用户使用jar包时如果需要自定义配置，请[点此下载](https://github.com/JunzhouLiu/BILIBILI-HELPER/blob/main/src/main/resources/config.json)配置文件，将其到和jar包同一目录即可，执行时优先加载外部配置文件**

```
BILIBILI-HELPER.jar
config.json
```

除此之外，也可以通过点击 [BILIBILI-HELPER/release](https://github.com/JunzhouLiu/BILIBILI-HELPER/releases)，下载已发布的版本，解压后将jar包手动上传到Linux服务器，使用crontab完成定时执行。

**命令格式解释：**

`30 10 * * * java -jar /home/BILIBILI-HELP.jar DEDEUSERID SESSDATA BILI_JCT SCKEY>/var/log/cron.log &`

| args                               | 说明                                                                                                       |
| ---------------------------------- | ---------------------------------------------------------------------------------------------------------- |
| 30 10 * * *                        | cron 定时时间                                                                                              |
| java -jar                          | 执行jar包                                                                                                  |
| /home/BILIBILI-HELP.jar            | jar包路径                                                                                                  |
| DEDEUSERID SESSDATA BILI_JCT SCKEY | 传入参数的顺序，参数含义请见上文,SCKEY可为空（用于server酱推送日志，等同actions任务配置中的SERVERPUSHKEY） |
| >/var/log/cron.log &               | 日志写入的路径                                                                                             |


**命令示例：**

```shell
# *如果Cookies参数中包含特殊字符，例如`%`请使用`\`转义*
# m h  dom mon dow   command
30 10 * * * java -jar /home/BILIBILI-HELP.jar DEDEUSERID SESSDATA BILI_JCT >/var/log/cron.log &
```

### 运行效果

![图示](docs/IMG/liunxImg.png)

## 三、使用 Windows10

### 步骤

1. 点击 [BILIBILI-HELPER/release](https://github.com/JunzhouLiu/BILIBILI-HELPER/releases)，下载已发布的版本。解压，在解压后的目录打开 `Powershell` 需要装有 Java 运行环境。
   
**Windows用户使用jar包时如果需要自定义配置，请[点此下载](https://github.com/JunzhouLiu/BILIBILI-HELPER/blob/main/src/main/resources/config.json)配置文件，将其到和jar包同一目录即可，执行时优先加载外部配置文件**

1. 执行 `java -jar /home/BILIBILI-HELP.jar DEDEUSERID SESSDATA BILI_JCT `

![图示](docs/IMG/powershell.png)

## 四、使用 Docker

请自行参阅 [Issues/75#issuecomment-731705657](https://github.com/JunzhouLiu/BILIBILI-HELPER/issues/75#issuecomment-731705657) 和[基于本项目的衍生项目](#基于本项目的衍生项目) 。

- **基于本项目的docker封装项目：[SuperNG6/docker-bilbili-helper](https://github.com/SuperNG6/docker-bilbili-helper)**

- **基于本项目的docker镜像：[superng6/bilbili-helper](https://hub.docker.com/r/superng6/bilbili-helper)**

# 微信订阅通知

## 订阅执行结果

1. 前往 [sc.ftqq.com](http://sc.ftqq.com/3.version) 点击登入，创建账号（建议使用 GitHub 登录）。
2. 点击点[发送消息](http://sc.ftqq.com/?c=code) ，生成一个 Key。将其增加到 Github Secrets 中，变量名为 `SERVERPUSHKEY`
3. [绑定微信账号](http://sc.ftqq.com/?c=wechat&a=bind) ，开启微信推送。
![图示](docs/IMG/serverpush.png)
4. 推送效果展示
![图示](docs/IMG/wechatMsgPush.png)


# 快速更新

## 使用 Github Actions 自动同步源仓库代码

参阅[Issue8 使用GitHub Actions任务自动同步源仓库代码](https://github.com/JunzhouLiu/BILIBILI-HELPER/issues/8)

## 手动拉取最新代码

参阅[Issues4 手动拉取最新代码](https://github.com/JunzhouLiu/BILIBILI-HELPER/issues/4)

# 常见问题解答

请参阅[常见问题解答](https://github.com/JunzhouLiu/BILIBILI-HELPER/issues/4)

# 免责声明

1. 本工具不会记录你的任何敏感信息，也不会上传到任何服务器上。（例如用户的cookies数据，cookies数据均存在Actions Secrets中或者用户自己的设备上）
2. 本工具不会记录任何执行过程中来自b站的数据信息，也不会上传到任何服务器上。（例如av号，bv号，用户uid等）。
3. 本工具执行过程中产生的日志，仅会在使用者自行配置推送渠道后进行推送。日志中不包含任何用户敏感信息。
4. 如果有人修改了本项目（或者直接使用本项目）盈利恰饭，那和我肯定没关系，我开源的目的单纯是技术分享。
5. 如果你使用了第三方修改的，打包的本工具代码，那你可得注意了，指不定人就把你的数据上传到他自己的服务器了，这可和我没关系。（**网络安全教育普及任重而道远**）
6. 本工具源码仅在[JunzhouLiu/BILIBILI-HELPER](https://github.com/JunzhouLiu/BILIBILI-HELPER)开源，其余的地方的代码均不是我提交的，可能是抄我的，借鉴我的，但绝对不是我发布的，出问题和我也没关系。 
7. 我开源本工具的代码仅仅是技术分享，没有任何丝毫的盈利赚钱目的，我连赞赏都移除了（因为挂半个月压根没人给我赞赏），如果你非要给我打赏，那我就是网络乞丐，咱俩可没任何py交易啊。
8. 本项目遵守[MIT License](https://github.com/JunzhouLiu/BILIBILI-HELPER/blob/main/LICENSE) ，请各位知悉。


# 致谢
感谢 JetBrains 对本项目的支持。

[![JetBrains](docs/IMG/jetbrains.svg)](https://www.jetbrains.com/?from=BILIBILI-HELPER)

# API 参考列表

- [SocialSisterYi/bilibili-API-collect](https://github.com/SocialSisterYi/bilibili-API-collect)
- [happy888888/BiliExp](https://github.com/happy888888/BiliExp)

# 基于本项目的衍生项目

- **基于本项目的docker封装项目：[SuperNG6/docker-bilbili-helper](https://github.com/SuperNG6/docker-bilbili-helper)**

- **基于本项目的docker镜像：[superng6/bilbili-helper](https://hub.docker.com/r/superng6/bilbili-helper)**
