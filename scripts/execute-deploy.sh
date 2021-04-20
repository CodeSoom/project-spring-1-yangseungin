#!/bin/bash

echo "> 실행권한 추가"
sudo chmod +x /home/ec2-user/app/deploy/switch.sh
sudo chmod +x /home/ec2-user/app/deploy/deploy.sh
/home/ec2-user/app/deploy/deploy.sh > /dev/null 2> /dev/null < /dev/null &
