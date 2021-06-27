 sshpass -f password ssh sd205@l040101-ws$1.ua.pt << EOF
        lsof -i -P -n | grep LISTEN
EOF
