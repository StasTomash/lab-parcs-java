all: run

clean:
	rm -f out/Main.jar out/Runner.jar

out/Main.jar: out/parcs.jar src/*.java
	@javac -cp out/parcs.jar src/*.java
	@jar cf out/Main.jar -C src Main.class -C src Runner.class -C src DataChunk.class
	@rm -f src/*.class

out/Runner.jar: out/parcs.jar src/Runner.java src/DataChunk.java
	@javac -cp out/parcs.jar src/Runner.java src/DataChunk.java
	@jar cf out/Runner.jar -C src Runner.class -C src DataChunk.class
	@rm -f src/Runner.class src/DataChunk.class

build: out/Main.jar out/Runner.jar

run: out/Main.jar out/Runner.jar
	@python3 generator.py
	@cd out && java -cp parcs.jar:Main.jar Main