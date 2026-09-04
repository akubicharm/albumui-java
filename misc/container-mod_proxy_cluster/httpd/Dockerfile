FROM registry.redhat.io/ubi10/httpd-24:1787777094

USER 0

# install mod_proxy_cluster
COPY mod_proxy_cluster-1.3.22-2.el10.aarch64.rpm /tmp/mod_proxy_cluster-1.3.22-2.el10.aarch64.rpm
RUN dnf install -y /tmp/mod_proxy_cluster-1.3.22-2.el10.aarch64.rpm && rm -f /tmp/mod_proxy_cluster-1.3.22-2.el10.aarch64.rpm

# unload lbmethod_heartbeat_module, proxy_balancer_module to avoid conflict with mod_proxy_cluster
RUN grep -v proxy_balancer_module  /etc/httpd/conf.modules.d/00-proxy.conf > /etc/httpd/conf.modules.d/00-proxy.conf.new && \
mv /etc/httpd/conf.modules.d/00-proxy.conf.new /etc/httpd/conf.modules.d/00-proxy.conf && \
grep -v lbmethod_heartbeat_module /etc/httpd/conf.modules.d/00-proxy.conf > /etc/httpd/conf.modules.d/00-proxy.conf.new && \
mv /etc/httpd/conf.modules.d/00-proxy.conf.new /etc/httpd/conf.modules.d/00-proxy.conf

COPY mod_proxy_cluster.conf /etc/httpd/conf.d/mod_proxy_cluster.conf
RUN mkdir /var/cache/mod_proxy_cluster && chown -R 1001:0 /var/cache/mod_proxy_cluster

USER 1001