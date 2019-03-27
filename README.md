# fake-kms

A simple aplication that has an HTTP api compatible with AWS Key Management Service

## Features available

- Encrypt (since version 1)
- Decrypt (since version 1)

## How to run?

Even tho this is a JVM project, we only provide a docker image:

```
docker run -p 9000:9000 gabfssilva/fake-kms:latest
```

## How to use a custom port?

```
docker run -p 8080:9000 gabfssilva/fake-kms:latest

#or, you can pass using an environment variable

docker run --env APP_PORT=8080 -p 8080:8080 gabfssilva/fake-kms:latest
```

## What did you use to build it?

Basically, we used:

- Scala 2.12.8
- Akka HTTP 10.1.+
- Circe 0.11.+
- AWS SDK 2.+

If you check the docker image out, you'll see we're using [OpenJ9](https://github.com/eclipse/openj9), a low memory footprint JVM created by IBM and now maintained by Eclipse
 
<<<<<<< HEAD
### Why are using OpenJ9?
=======
### Why are you using OpenJ9?
>>>>>>> b9604b7b529075997d2317b4b1b8067387062d55

Because the memory footprint of fake-kms on OpenJ9 is somewhere between 64 and 128mb

## How to build it myself?

```
git clone https://github.com/gabfssilva/fake-kms.git
cd fake-kms
sbt clean build assembly
docker build -t fake-kms .

#if you want to run:
docker run -p 9000:9000 fake-kms
```

## How can I help?

PR's are very welcome, so, if you want to help, fork this project and check [the issues](https://github.com/gabfssilva/fake-kms/issues) out. There are tons of actions that we don't implement yet.
Of course, if you don't have time or simply don't want to, just add a comment in the desired feature in [the issues](https://github.com/gabfssilva/fake-kms/issues) and probably this will be our priority.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

