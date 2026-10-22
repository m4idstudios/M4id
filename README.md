<div align="center">

<img src="src/main/resources/assets/icon.png" alt="M4id logo" width="128" height="128">

# M4id Library

**A Kotlin toolkit for Minecraft mods on Fabric and NeoForge.**
Write commands, networking, config, and registration once, with Kotlin DSLs, and run them on both loaders. Used and required for my mods.

[![JitPack](https://jitpack.io/v/DoctorM4id/M4id.svg)](https://jitpack.io/#DoctorM4id/M4id)
[![License: MPL-2.0](https://img.shields.io/badge/license-MPL--2.0-blue.svg)](LICENSE)
![Minecraft](https://img.shields.io/badge/Minecraft-1.21.1-62B47A)
![Loaders](https://img.shields.io/badge/loaders-Fabric%20%7C%20NeoForge-DBB69B)
![Kotlin](https://img.shields.io/badge/Kotlin-2.x-7F52FF?logo=kotlin&logoColor=white)
![Status](https://img.shields.io/badge/status-alpha-orange)

[Features](#features) · [Install](#install) · [Quick start](#quick-start) · [Compatibility](#compatibility) · [Building](#building-from-source) · [Credits](#credits)

</div>

> [!NOTE]
> M4id is in **alpha**. APIs can and will change between releases until 1.0.

---

## Features

| Area | What you get | Entry point |
|---|---|---|
| **Commands** | A Brigadier DSL: nested literals, typed arguments, permission helpers, suggestion providers | `M4id.Command` · `dispatcher.addCommand(...)` |
| **Networking** | Serializable message classes with client and server handlers, no manual packet codecs | `M4id.Message` · `M4idMsg` |
| **Data files** | Config and per-server data as plain Kotlin properties, with atomic saves and corrupt-file backups | `DataFile.forClient` · `DataFile.forServer` |
| **Serialization** | `kotlinx.serialization` support for `ResourceLocation`, `Vec3`, `AABB`, `CompoundTag`, and `ItemStack` | `M4id.Serial` |
| **Registration** | Declare items, blocks, entities, effects, components, and more with one-line registrars | `M4idAutoRegistrar` |
| **Text** | A builder DSL for styled chat components, with click and hover events | `textLiteral { ... }` |
| **Input** | Keybinds with modifier keys and down/up callbacks | `M4id.Input` |
| **Criteria** | Hook advancement triggers from code | `M4id.Criterion` |
| **Logging** | Per-mod loggers and markers | `M4id.Logging` |
| **Percale** | Bridges between Mojang `Codec`s, NBT, Gson, and `kotlinx.serialization` | `dev.doctorm4id.percale` |

---

## Install

M4id is published through [JitPack](https://jitpack.io/#DoctorM4id/M4id). Each Minecraft version and loader is its own artifact, named `<minecraft>-<loader>` (for example `1.21.1-fabric`).

**Gradle (Kotlin DSL)**

```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    // Fabric
    modImplementation("com.github.DoctorM4id.M4id:1.21.1-fabric:0.1.0-alpha.4")

    // NeoForge
    implementation("com.github.DoctorM4id.M4id:1.21.1-neoforge:0.1.0-alpha.4")
}
```

The JitPack page shows the exact snippet for each tag. See the [tags](https://github.com/DoctorM4id/M4id/tags) for available versions.

**Loader notes**

- **Fabric:** the Kotlin standard library and `kotlinx.serialization` are bundled into the jar.
- **NeoForge:** requires [Kotlin for Forge](https://github.com/thedarkcolour/KotlinForForge) at runtime.

---

## Quick start

Everything hangs off the `M4id` object. (hopefully)

### Commands

```kotlin
import dev.doctorm4id.m4id.command.addCommand
import dev.doctorm4id.m4id.command.requiresOp

CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
    dispatcher.addCommand("hello") {
        requiresOp(2)

        "greet" {
            argString("name") { name ->
                this runs {
                    source.sendSystemMessage(Component.literal("Hello, ${name()}!"))
                }
            }
        }
    }
}
```

Running `/hello greet Steve` prints `Hello, Steve!`. Typed arguments include `argInt`, `argBool`, `argPlayer`, `argBlockPos`, `argVec3`, `argResource`, and more.

### Config and data files

```kotlin
object MyConfig {
    private val file = DataFile.forClient(File("config/mymod.json"))

    var volume by file.of<Float> { 0.5f }
    var greeting by file.of<String>("greeting") { "Hello" }
}
```

Values are read lazily, written atomically, and a file that fails to parse is copied to `*.corrupt` instead of being overwritten. Use `DataFile.forServer(...)` for data that should load and save with the world.

### Messages

```kotlin
import dev.doctorm4id.m4id.ext.ResourceLocation

@Serializable
data class HelloMsg(val text: String) : M4idMsg() {
    override fun onClientReceived() {
        Minecraft.getInstance().player?.sendSystemMessage(Component.literal(text))
    }
}

// During initialization
M4id.Message.registerClientMessage<HelloMsg>(ResourceLocation("mymod", "hello"))

// Anywhere on the server
HelloMsg("Welcome!").sendToClient(player)
```

Use `registerServerMessage` and `sendToServer()` for the other direction.

### Registration

```kotlin
object MyContent : M4idAutoRegistrar {
    override fun getId() = "mymod"

    val RUBY = "ruby" forItem { Item(Item.Properties()) }
    val RUBY_BLOCK = "ruby_block" forBlock { Block(BlockBehaviour.Properties.of()) }
}

// During initialization
M4idRegistrar.doRegistrationsFor(MyContent)
```

### Styled text

```kotlin
val message = textLiteral("Config ") {
    addLiteral("saved") {
        bold = true
        color = 0x55FF55
    }
    addLiteral("!")
}
player.sendSystemMessage(message)
```

### Serialization

```kotlin
@Serializable
data class Waypoint(
    val name: String,
    @Contextual val pos: Vec3,
    @Contextual val dimension: ResourceLocation,
)

val json = M4id.Serial.Format
val encoded = json.encodeToString(Waypoint.serializer(), waypoint)
```

---

## Compatibility

| Minecraft | Fabric | NeoForge | Status |
|---|:---:|:---:|---|
| 1.21.1 | ✅ | ✅ | Supported |
| 1.20.1 | planned | n/a | Planned, as needed |
| 26.1 and newer | planned | planned | Planned |

Versions between these are skipped. The full matrix, including the latest release per version, is in [SECURITY.md](SECURITY.md).

Requires **Java 21** for Minecraft 1.20.5 and newer. (I think.)

---

## Building from source

M4id uses [Stonecutter](https://stonecutter.kikugie.dev/) to build Minecraft versions and loaders from one source tree.

```bash
git clone https://github.com/DoctorM4id/M4id.git
cd M4id

./gradlew build                      # build everything
./gradlew runActiveClient            # run the active version's client
./gradlew runActiveServer            # run the active version's server
./gradlew publishM4idToMavenLocal    # install all artifacts locally
```

The active version lives in `.sc_active_version` (for example `1.21.1-fabric`). Use the build commands under Stonecutter to switch versions and/or loaders.

<details>
<summary><b>Project layout</b></summary>

```
M4id/
├── build-logic/                  Convention plugin: manifests, publishing, mixins
├── build.fabric.gradle.kts       Fabric build script
├── build.neoforge.gradle.kts     NeoForge build script
├── stonecutter.properties.toml   Mod metadata and per-version dependencies
└── src/fabric/                   Fabric stuff.
└── src/neoforge/                 Neoforge stuff.
└── src/main/
    ├── java/dev/doctorm4id/
    │   ├── m4id/
    │   │   ├── command/          Command DSL
    │   │   ├── message/          Networking
    │   │   ├── data/             Config and data files
    │   │   ├── serial/           Serializers
    │   │   ├── registration/     Auto-registrar
    │   │   ├── text/             Text builder
    │   │   ├── input/            Keybinds
    │   │   ├── ext/              Extension functions
    │   │   └── platform/         Fabric and NeoForge implementations
    │   │   └── util/             Random ass utility. -w-
    │   │   └── internal/         Mixins, Tests.
    │   └── percale/              Codec, NBT, and Gson bridges
    └── resources/                Mixins, access wideners, assets
```

</details>

---

## Support

- 💬 **Community server:** [Discord](https://discord.doctorm4id.dev)
- 🐛 **Bugs and requests:** [open an issue](https://github.com/DoctorM4id/M4id/issues)
- 🔒 **Security issues:** see [SECURITY.md](SECURITY.md)

---

## Credits

M4id is a fork of **Kambrik** and **Percale**. Thanks to the original authors. ❤️

## License

Licensed under the [Mozilla Public License 2.0](LICENSE).
