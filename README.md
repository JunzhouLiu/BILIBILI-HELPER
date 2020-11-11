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

这是一个利用 Linux Crontab , GitHub Action 等实现哔哩哔哩（Bilibili）每日自动投币，点赞，分享视频，直播签到，银瓜子兑换硬币，漫画每日签到，简单配置即可每日轻松获取 65 经验值，快来和我一起成为 Lv6 吧~~~~

**如果觉得好用，顺手点个 Star 吧 ❤**

**仓库地址：[JunzhouLiu/BILIBILI-HELPER](https://github.com/JunzhouLiu/BILIBILI-HELPER)**

## 功能列表

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
......

[点击快速开始使用](#快速开始使用)

# 目录

- [工具简介](#工具简介)
  - [功能列表](#功能列表)
- [目录](#目录)
- [使用说明](#使用说明)
  - [一、Actions 定时任务（推荐）](#一actions-定时任务推荐)
    - [配置自定义功能](#配置自定义功能)
    - [查看运行日志](#查看运行日志)
  - [二、使用 Linux Crontab 方式](#二使用-linux-crontab-方式)
    - [步骤](#步骤)
    - [运行效果](#运行效果)
  - [三、使用 Windows10](#三使用-windows10)
    - [步骤](#步骤-1)
- [微信订阅通知](#微信订阅通知)
  - [订阅执行结果](#订阅执行结果)
  - [订阅版本更新](#订阅版本更新)
- [快速更新](#快速更新)
  - [关于项目更新频率](#关于项目更新频率)
  - [使用 Github Actions 自动同步源仓库代码](#使用-github-actions-自动同步源仓库代码)
  - [手动拉取最新代码](#手动拉取最新代码)
- [常见问题解答](#常见问题解答)
- [致谢](#致谢)
- [API 参考列表](#api-参考列表)

# 使用说明

## 一、Actions 定时任务（推荐）

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

### 配置自定义功能

**配置文件位于 `src/main/resources/config.json`**

参数示意

| Key                | Value         | 说明                                                      |
| ------------------ | ------------- | --------------------------------------------------------- |
| numberOfCoins      | [0,5]         | 每日投币数量,默认 5                                       |
| selectLike         | [0,1]         | 投币时是否点赞，默认 0, 0：否 1：是                       |
| ~~watchAndShare~~  | ~~[0,1]~~     | ~~观看时是否分享~~                                        |
| monthEndAutoCharge | [false,true]  | 年度大会员月底是否用 B币券 给自己充电，默认 `true`        |
| devicePlatform     | [ios,android] | 手机端漫画签到时的平台，建议选择你设备的平台 ，默认 `ios` |

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

### 运行效果

![图示](docs/IMG/liunxImg.png)

## 三、使用 Windows10

### 步骤

1. 点击 [BILIBILI-HELPER/release](https://github.com/JunzhouLiu/BILIBILI-HELPER/releases)，下载已发布的版本。解压，在解压后的目录打开 `Powershell` 需要装有 Java 运行环境。
   
**Windows用户使用jar包时如果需要自定义配置，请[点此下载](https://github.com/JunzhouLiu/BILIBILI-HELPER/blob/main/src/main/resources/config.json)配置文件，将其到和jar包同一目录即可，执行时优先加载外部配置文件**

1. 执行 `java -jar /home/BILIBILI-HELP.jar DEDEUSERID SESSDATA BILI_JCT `

![图示](docs/IMG/powershell.png)

# 微信订阅通知

## 订阅执行结果

1. 前往 [sc.ftqq.com](http://sc.ftqq.com/3.version) 点击登入，创建账号（建议使用 GitHub 登录）。
2. 点击点[发送消息](http://sc.ftqq.com/?c=code) ，生成一个 Key。将其增加到 Github Secrets 中，变量名为 `SERVERPUSHKEY`
3. [绑定微信账号](http://sc.ftqq.com/?c=wechat&a=bind) ，开启微信推送。
![图示](docs/IMG/serverpush.png)
4. 推送效果展示
![图示](docs/IMG/wechatMsgPush.png)

## 订阅版本更新

微信扫描以下二维码进入用户反馈群（通道推送已移除）

![图示](docs/IMG/wechatGroup.jpg)

# 快速更新

## 关于项目更新频率

目前处于快速迭代阶段，建议通过以下两种方式从本仓库拉取最新代码。

## 使用 Github Actions 自动同步源仓库代码

该方案来自 `@happy888888` `#PR6` ，由于源仓库 `config.json` 文件的更改会覆盖用户自己的 `config.json` 配置文件，所以暂时没有合并到 `main` 分支。

**使用自定义功能的朋友慎用该方法，建议使用手动拉取的方式，手动解决代码冲突**

在 `./github/workflows` 目录下创建 `auto_merge.yml` 文件，内容如下

```yml
name: auto_merge

on:
  workflow_dispatch:
  schedule:
    - cron: 0 2 * * fri
    # cron表达式,每周五10点执行一次，UTC时间，使用北京时间请+8可按照需求自定义。  

jobs:
  merge:
    runs-on: ubuntu-latest
    steps:
    - name: Checkout
      uses: actions/checkout@v2
      with:
        ref: main
        fetch-depth: 0
        lfs: true

    - name: Set git identity
      run : |
        git config --global user.email "41898282+github-actions[bot]@users.noreply.github.com"
        git config --global user.name "github-actions[bot]"
    - name: Load upstream commits
      run: |
        git update-index --assume-unchanged ./src/main/resources/config.json
        git pull https://github.com/JunzhouLiu/BILIBILI-HELPER.git --log --no-commit
    - name: Apply commit changes
      run: |
        if [ -f ./.git/MERGE_MSG ]; then
        mkdir ./tmp && cp ./.git/MERGE_MSG ./tmp/message
        sed -i "1c [bot] AutoMerging: merge all upstream's changes:" ./tmp/message
        sed -i '/^\#.*/d' ./tmp/message
        git commit --file="./tmp/message"
        else
        echo "There is no merge commits."
        fi
    - name: Push Commits
      env:
        DOWNSTREAM_BRANCH: main
        TZ: Asia/Shanghai
      run: git push origin $DOWNSTREAM_BRANCH
```

## 手动拉取最新代码

1. 通过 `git remote -v` 查看是否有源头仓库的别名和地址。

例如这里 `origin` 就是你自己的仓库，`upstream` 是你 `fork` 的源头仓库。

```bash
$ git remote -v
origin  https://github.com/JunzhouLiu/cxmooc-tools.git (fetch)
origin  https://github.com/JunzhouLiu/cxmooc-tools.git (push)
upstream        https://github.com/CodFrm/cxmooc-tools.git (fetch)
upstream        https://github.com/CodFrm/cxmooc-tools.git (push)

```

2. fork 仓库后，将你的仓库拉到本地，如果没有源头仓库，则添加源头仓库

```bash
git remote add upstream https://github.com/JunzhouLiu/BILIBILI-HELPER.git
```

3. 更新上游仓库 main 分支的代码（pull 操作实际上是 `fetch+merge`）

```bash
git pull upstream main
```

4. 将从源头仓库更新后的代码推送到你自己的 GitHub 仓库

```bash
git push origin main
```

5. 这样你就能快速的从我的仓库拉取最新的代码，并更新到你自己的仓库里了。自定义配置的同学，要注意 `config.json` 不要被我的文件覆盖了。

# 常见问题解答

请参阅[常见问题解答](https://github.com/JunzhouLiu/BILIBILI-HELPER/issues/4)

# 致谢
感谢 JetBrains 对本项目的支持。

[![JetBrains](docs/IMG/jetbrains.svg)](https://www.jetbrains.com/?from=BILIBILI-HELPER)

# API 参考列表

- [SocialSisterYi/bilibili-API-collect](https://github.com/SocialSisterYi/bilibili-API-collect)
- [happy888888/BiliExp](https://github.com/happy888888/BiliExp)
