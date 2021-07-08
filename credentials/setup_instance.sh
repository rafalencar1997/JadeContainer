
ip="18.116.62.248Y"

echo $ip
scp -i "myKeyForJade.pem" run_experiments.sh ec2-user@$ip:run_experiments.sh