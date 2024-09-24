### Dev build

``` shell
docker-compose up --build  -d
```
---

### Production build
``` shell
docker-compose -f docker-compose.yml -f docker-compose.production.yml up --build -d
```