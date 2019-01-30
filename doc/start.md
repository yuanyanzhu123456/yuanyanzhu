# webApp start 

### java -jar  /server/apps/rcs2_server/spring-boot-freemarker.jar  --spring.profiles.active=test --server.port=8020

# schedule start 

### java -jar  /server/apps/rcs2_server/spring-boot-freemarker.jar  --spring.profiles.active=test --spring.main.web_environment=false  --app=scheduler

# worker start 

### java -jar  /server/apps/rcs2_server/spring-boot-freemarker.jar  --spring.profiles.active=test --spring.main.web_environment=false  --app=worker  --workname=rcs2_worker --taskrole=api


# jobCli start 

### java -jar  /server/apps/rcs2_server/spring-boot-freemarker.jar  --spring.profiles.active=gray --spring.main.web_environment=false --app=JobCli

# abEventWorker

### --spring.main.web_environment=false  --app=worker  --workname=AbEventWorker001   --taskrole=abEvent