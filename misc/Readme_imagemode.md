# Apache Http server + Tomcat の連携 (Image Mode for RHEL)

(Readme.md)[Readme.md] の手順に従って、bootc をベースとしたコンテナイメージを作成し、KVM で実行sる。

1. qcow2 のイメージ作成

```
podman run --rm --privileged \
        --volume .:/output \
        --volume ./config.json:/config.json \
        --volume /var/lib/containers/storage:/var/lib/containers/storage \
        registry.redhat.io/rhel10/bootc-image-builder:10.2 \
        --type qcow2 \
        --config config.json \
        <container repo url>/tomcat
```


2. KVM で実行

ディスクイメージのインポート

```
cp qcow2/disk.qcow2 /var/lib/libvirt/images/bootc-vm.qcow2

virt-install --name bootc-vm \
--disk /var/lib/libvirt/images/bootc-vm.qcow2 \
--import \
--memory 2048 \
--graphics none \
--osinfo rhel10-unknown \
--noautoconsole \
--noreboot

virsh start bootc-vm
```