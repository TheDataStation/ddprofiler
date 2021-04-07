DDPROFILER = ./build/install/ddprofiler/bin/ddprofiler
ES_CONTAINER = es-container
KIBANA_CONTAINER = kibana-container
DOCKER = docker

start-es:
	$(DOCKER) run -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" --name $(ES_CONTAINER) docker.elastic.co/elasticsearch/elasticsearch:6.8.14

start-kibana:
	docker run --link $(ES_CONTAINER):elasticsearch -p 5601:5601 docker.elastic.co/kibana/kibana:6.8.14

start-catalog-service:
	# Start gunicorn service here

profile-chemball.yml:
	$(DDPROFILER) --sources ./chemball.yml

profile-chemball-mini.yml:
	# Removed public.activities.csv public.compound_structures.csv
	$(DDPROFILER) --sources ./chemball-mini.yml

clean:
	- docker stop $(ES_CONTAINER) $(KIBANA_CONTAINER)
	- docker rm $(ES_CONTAINER) $(KIBANA_CONTAINER)
