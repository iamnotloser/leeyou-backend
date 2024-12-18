# Docker 镜像构建
# @author <a href="https://github.com/liyupi">程序员鱼皮</a>
# @from <a href="https://yupi.icu">编程导航知识星球</a>
FROM java:8

# Copy local code to the container image.
VOLUME /tmp


# Build a release artifact.
ADD leeyou_backend-0.0.1-SNAPSHOT.jar app.jar

# Run the web service on container startup.
RUN bash -c 'touch /app.jar'

CMD ["java","-jar","/app.jar","--spring.profiles.active=prod"]

# [编程学习交流圈](https://www.code-nav.cn/) 快速入门编程不走弯路！30+ 原创学习路线和专栏、500+ 编程学习指南、1000+ 编程精华文章、20T+ 编程资源汇总