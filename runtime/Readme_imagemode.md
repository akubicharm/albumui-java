# Apache Http server + Tomcat の連携 (Image Mode for RHEL)

(Readme.md)[Readme.md] の手順に従って、bootc をベースとしたコンテナイメージを作成し、KVM で実行する。

1. qcow2 のイメージ作成

```
podman run --rm --privileged \
        --volume .:/output \
        --volume ./config.json:/config.json \
        --volume /var/lib/containers/storage:/var/lib/containers/storage \
        registry.redhat.io/rhel10/bootc-image-builder:10.2 \
        --type qcow2 \
        --config config.json \
        builder-nb7pn.apps.ocpv07.rhdp.net/httpd:dev        
        <container repo url>/tomcat
```


2. KVM で実行

ディスクイメージのインポートして実行

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

virsh --connect qemu:///system start bootc-vm
```


## トラブルシューティング

- 登録済みのゲストを削除する方法

```
virsh list --all
virsh shutdown bootc-vm
virsh destroy bootc-vm
virsh undefine bootc-vm --remove-all-storage
```