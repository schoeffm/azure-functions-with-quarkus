# Azure Function with Quarkus

Quarkus offers a neat integration with [HTTP-triggered Azure Functions][qrk-fn] - the only downside is that you're limited to HTTP triggers.

This app is basically a re-write of [this blog posts][blog] app (just to get my hands dirty myself).

## Creating and building
Using the `quarkus-azure-functions-http-archetype` you can generate a project skeleton that is ready to be used:
```bash
mvn archetype:generate -B \
  -DarchetypeGroupId=io.quarkus \
  -DarchetypeArtifactId=quarkus-azure-functions-http-archetype \
  -DarchetypeVersion=999-SNAPSHOT \           # use a proper/fixed version
  -DjavaVersion=11 \
  -DgroupId=de.bender.fn.quarkus \
  -DartifactId=azure-functions-with-quarkus \
  -Dversion=1.0.0-SNAPSHOT \
  -DappName=fn-quarkus-test \
  -DappRegion=westeurope \
  -Dfunction=fn-quarkus-test \
  -DresourceGroup=rg-fn-quarkus-test
  
mvn clean install azure-functions:run         # run it locally
mvn clean install azure-functions:deploy      # deploy it
```

> **Notice:** All archetype-versions up to and including `2.6.1.Final` [are buggy][bug]. I've [provided a PR][pr] which fixes the current issues - waiting for it to get merged.

- the archetype generates a bunch of http-integrations (`vertex`, `funq`, `resteasy` as well as `jax-rs`- **remove** what you don't like to use)
- if you change the version in your generated `pom.xml` don't forget to also adjust the jar-reference in `azure-config/function.json` accordingly.

## Interacting with Azure

Since we cannot use bindings we have to resort to SDKs instead. That's not too big of an issue as my [plain java function attempts][java-fn] have already shown that you hardly can write a non-trivial app without resorting to SDKs anyways.

For this app (followin' this [awesome blog post][blog]) I used the [Azure Resource Manager API][resourcemanager-sdk-resource]. In order to do that you have to define a few env-variables (in order to authenticate: `AZURE_CLIENT_ID`, `AZURE_CLIENT_SECRET` and `AZURE_TENANT_ID`).

It's worth to explore the other [resource-manager APIs in this github-repo][resourcemanager-sdk] as well as the [samples section][resourcemanager-sdk-samples].

## Remarks
- you can still use learnings/concepts demo'ed in the [plain java functions][java-fn] approach
  - i.e. `proxies.json` or the integration with other SDKs (like Table Storage).
  - since we're still using `azure-functions-maven-plugin` remember the proxy settings when deploying from behind a (corp) proxy


[qrk-fn]:https://quarkus.io/guides/azure-functions-http
[qrk-repo]:https://github.com/quarkusio/quarkus/tree/main/extensions/azure-functions-http
[blog]:https://blog.nebrass.fr/playing-with-azure-functions-and-quarkus/
[bug]:https://github.com/quarkusio/quarkus/issues/20814
[pr]:https://github.com/quarkusio/quarkus/pull/22578
[java-fn]:https://github.com/schoeffm/azure-functions-in-java
[resourcemanager-sdk]:https://github.com/Azure/azure-sdk-for-java/tree/main/sdk/resourcemanager
[resourcemanager-sdk-resource]:https://github.com/Azure/azure-sdk-for-java/tree/main/sdk/resourcemanager/azure-resourcemanager-samples/src/main/java/com/azure/resourcemanager
[resourcemanager-sdk-samples]:https://github.com/Azure/azure-sdk-for-java/tree/main/sdk/resourcemanager/azure-resourcemanager-samples/src/main/java/com/azure/resourcemanager