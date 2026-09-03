# VM環境を想定した実行環境

## アーキテクチャ

```mermaid
architecture-beta
    group web_layer(server)[Web Layer]
    group app_layer(server)[App Layer]

    service web(internet)[Web Client]
 
    service apache(server)[Apache HTTP Server with mod_proxy_cluster] in web_layer

    service tomcat1(server)[Tomcat with modClusterListener] in app_layer
    service tomcat2(server)[Tomcat with modClusterListener] in app_layer

    web:B --> T:apache
    apache:B --> T:tomcat1
    apache:B --> T:tomcat2
```

### Apache HTTP Server

mod_proxy_clusterを利用して、App層のTomcatへのロードバランサーの役割を果たす。

mod_proxy_clusterを利用する場合
- mod_proxy_clusterは、httpdのコンテナイメージには含まれていないので、別途インストールする
- mod_proxy_clusterを使う場合、mod_proxy_balancerモジュールを無効にする(/etc/httpd/conf.modules.d/00-proxy.conf からエントリを削除)


`mod_proxy_cluster.conf` でTomcatからのクラスターへの参加リクエストを受け付ける manager module の設定を定義。
このデモ環境の設定では、manager moduleは 8090 ポートでリクエストを待つ。リクエストの送信元を許可する設定の　Require ip にマッチしないホストからのリクエストは受け付けないので、ひとまず `all granted` にしている。


mod_proxy_cluster 設定の詳細は、[Red Hat JBoss Core ServicesのApache HTTPServerコネクターおよび負荷分散ガイド](https://docs.redhat.com/ja/documentation/red_hat_jboss_core_services/2.4.62/html/apache_http_server_connectors_and_load_balancing_guide/index)を参照。

### Tomcat

ウェブアプリケーションを提供するサーバ。
mod_proxy_clusterのリスナーとして登録するための設定を `server.xml` に設定。proxyListは apche http serverのホスト名 or IPアドレスと、manager moduleがリッスンしているポートを指定。

```
  <Listener className="org.jboss.modcluster.container.catalina.standalone.ModClusterListener" proxyList="apache:8090"/>

```

## 動作確認

Macでも実行できるようにコンテナ環境を利用。

コンテナイメージは[Red Hat Ecosystem Catalog](https://catalog.redhat.com/en/search?searchType=Containers) で公開されているもの。

### Apache　単体

`httpd/Dockerfile` でコンテナイメージを作成して実行

```sh
podman build . -t httpd 
```

```sh
podman run --rm --name apache -p 8080:8080 -p 8090:8090 httpd
```

(mod_proxy_clusterを正規の方法でインストールしていないので、Fedora Projectで公開されているmod_proxy_clusterを使っている。。。https://packages.fedoraproject.org/pkgs/mod_proxy_cluster/mod_proxy_cluster/

正しくインストールするならば、`dnf install mod_proxy_cluster`)

### Tomcat 単体

#### nativeで環境で実行する場合

JBoss Web Server を[Red Hat Customer Portal](https://access.redhat.com/downloads/content/application-services/core.service.apachehttp) からダウンロード。

`conf/server.xml` を [./tomcat/server.xml](./tomcat/server.xml) に置き換える。

- 起動と停止
```sh
jws-7.0/tomcat/bin/startup.sh
jws-7.0/tomcat/bin/shutdown.sh
```

#### コンテナ環境で実行する場合

Red Hat Container CatalogにJDK25バージョンのJBoss Web Serverが公開されていないので（as of 2026/9/1)、OpenJDKのイメージにJWSをインストールし、アプリケーション配備したコンテナイメージを作成。
※Tomcatの実行に使う JDK とアプリケーションのJDKのバージョンが違っていると 404 エラーになる

(ベースイメージはRed Hat Hardened Imageを使ってみたかっただけなので、普通の ubi でもOK。アプリのビルド時のJDKの馬ジョンが21 ならばOpenJDK21版のJWSのコンテナイメージが提供されているのでそちらを利用すれば良い）


```sh
podman build . -t tomcat
```

```sh
podman run --rm --name tomcat -p 8080:8080 tomcat
```


### Apache + Tomcat の環境

Apache と Tomcat が双方向で通信できる状態の必要があるので `podman compose` を使って、両方のコンテナが同一の仮想ネットワーク(Bridge Network)に接続して起動する。

```
podman network create mynet
podman compose up
```


