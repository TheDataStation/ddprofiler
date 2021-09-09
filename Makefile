DDPROFILER = ./build/install/ddprofiler/bin/ddprofiler
CATALOG_SERVICE_CONTAINER = cat-service
ES_CONTAINER = es-container
KIBANA_CONTAINER = kibana-container
DOCKER = docker
NETWORK_BUILDER = network-builder-container
GRADLEW = ./gradlew

start-es:
	$(DOCKER) run -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" --name $(ES_CONTAINER) docker.elastic.co/elasticsearch/elasticsearch:6.8.14

start-kibana:
	docker run --link $(ES_CONTAINER):elasticsearch -p 5601:5601 docker.elastic.co/kibana/kibana:6.8.14

start-catalog-service:
	# Start gunicorn service here
	# TODO start via docker.

.PHONY: profile-chemball.yml
profile-chemball.yml:
	$(DDPROFILER) --sources ./chemball.yml #--catalog_uri "https://localhost:5000"
	#$(NETWORK_BUILDER) --sources ./chemball.yml #--catalog_uri "https://localhost:5000"

profile-chemball-mini.yml:
	# Removed public.activities.csv public.compound_structures.csv
	$(DDPROFILER) --sources ./chemball-mini.yml --catalog_uri "https://localhost:5000"
	$(NETWORK_BUILDER) --sources ./chemball-mini.yml --catalog_uri "https://localhost:5000"

build:
	$(GRADLEW) clean build -x test installDist

clean:
	- $(GRADLEW) clean

 	#
	- docker stop $(ES_CONTAINER) $(KIBANA_CONTAINER)
	- docker rm $(ES_CONTAINER) $(KIBANA_CONTAINER)

demo: clean build profile-chemball.yml
