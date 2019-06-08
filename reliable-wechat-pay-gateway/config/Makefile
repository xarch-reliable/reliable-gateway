VERSION=0.0.1
NAME=reliable-wechat-pay-gateway
PORT=8200
FILE=/var/spring-project/reliable8

create:config docker run

all:config restart

config:
	mkdir -p $(FILE)/$(NAME)
	cp -i ../target/$(NAME)-$(VERSION).jar $(FILE)/$(NAME)/app.jar

docker:
	docker build -t lnnupet/$(NAME):$(VERSION) .

run:
	docker run --name $(NAME) \
	--restart=always \
	-v $(FILE)/$(NAME)/app.jar:/app.jar \
	-v $(FILE)/logs/$(NAME):/reliable-logs \
	-p $(PORT):$(PORT) \
	-d lnnupet/$(NAME):$(VERSION)

restart:
	docker restart $(NAME)

remove:
	docker stop $(NAME) && docker rm $(NAME)
	docker rmi lnnupet/$(NAME):$(VERSION)