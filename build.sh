javac src/*.java -d ./out
cd out/
jar -cvfm user_server.jar ../MANIFEST.MF ./*
mv user_server.jar ../
