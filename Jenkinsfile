pipeline {
  agent any
  stages {
    stage("检出") {
      steps {
        checkout([
          $class: 'GitSCM',
          branches: [[name: GIT_BUILD_REF]],
          userRemoteConfigs: [[
            url: GIT_REPO_URL,
            credentialsId: CREDENTIALS_ID
        ]]])
      }
    }
    stage(' 上传到 generic 仓库') {
      steps {
        // 使用 fallcate 命令创建 10M 大小的文件 (持续集成默认的工作目录为 /root/workspace)
        sh "fallocate -l 10m my-generic-file"

        codingArtifactsGeneric(
          files: "BILIBILI-HELPER",
          repoName: "${GENERIC_REPO_NAME}",
        )
      }
    }
  }
}