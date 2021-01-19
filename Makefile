.PHONY: test

db/old:
	docker run -p 5432:5432 guaranteedrate/homework-pre-migration:1607545060-a7085621

db/new:
	docker run -p 5433:5432 guaranteedrate/homework-post-migration:1607545060-a7085621

db/diff:
	clojure -M -m homework.core

test:
	clj -M:test
