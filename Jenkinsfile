pipeline {
  agent any
  environment {
    TAG_LATEST = ''
  }
  stages {
    stage('检出') {
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
      stage("版本号") {
          steps {
              script {
                def jsonStr = readFile(file: './src/main/resources/release.json')
                def slurper = new groovy.json.JsonSlurper()
                def json = slurper.parseText(jsonStr)
                TAG_LATEST = "v${json.tag_latest}"
              }
          }
      }
      stage('构建') {
        steps {
          sh "mvn -B package assembly:single --file pom.xml -Dmaven.test.skip=true"
        }
      }
      stage('zip') {
        steps {
          sh """
mv -f ./target/BILIBILI-HELPER-*-jar-with-dependencies.jar BILIBILI-HELPER-${TAG_LATEST}.jar
zip BILIBILI-HELPER-${TAG_LATEST}.zip BILIBILI-HELPER-${TAG_LATEST}.jar
cp ./src/main/resources/config.json ./
zip BILIBILI-HELPER-${TAG_LATEST}.zip config.json
zip BILIBILI-HELPER-${TAG_LATEST}.zip LICENSE
zip BILIBILI-HELPER-${TAG_LATEST}.zip README.md

cp BILIBILI-HELPER-${TAG_LATEST}.zip BILIBILI-HELPER-latest.zip
cp BILIBILI-HELPER-${TAG_LATEST}.jar BILIBILI-HELPER-latest.jar
          """
        }
      }
      stage(' 上传到 generic 仓库') {
        steps {
          codingArtifactsGeneric(files: "BILIBILI-HELPER-latest.zip", repoName: "${GENERIC_REPO_NAME}")
          codingArtifactsGeneric(files: "BILIBILI-HELPER-latest.jar", repoName: "${GENERIC_REPO_NAME}")
          codingArtifactsGeneric(files: "BILIBILI-HELPER-${TAG_LATEST}.zip", repoName: "${GENERIC_REPO_NAME}", version: "${TAG_LATEST}")
          codingArtifactsGeneric(files: "BILIBILI-HELPER-${TAG_LATEST}.jar", repoName: "${GENERIC_REPO_NAME}", version: "${TAG_LATEST}")
        }
      }
    }
  }