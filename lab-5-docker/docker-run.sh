#!/usr/bin/bash

#docker build -t my-lab .
docker run --rm -it my-lab /bin/bash -c "java -jar /usr/src/myapp"

