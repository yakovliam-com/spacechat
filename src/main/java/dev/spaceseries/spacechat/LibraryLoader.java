package dev.spaceseries.spacechat;

import com.saicone.ezlib.Ezlib;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class LibraryLoader {

    private static final String MAVEN_REPOSITORY = "https://repo.maven.apache.org/maven2/";
    private static final String DOT_ALIAS = "{}";
    private static final String PACKAGE_ALIAS = "{package}";

    private final JavaPlugin plugin;
    private final Ezlib ezlib;

    public LibraryLoader(JavaPlugin plugin) {
        this.plugin = plugin;
        this.ezlib = new Ezlib(new File(plugin.getDataFolder(), "libs")) {
            // Support for msg library
            @Override
            public File download(String dependency, String repository) throws IOException, IllegalArgumentException {
                String[] split = dependency.split(":", 4);
                if (split.length < 3) {
                    throw new IllegalArgumentException("Malformatted dependency");
                }

                String repo = repository.endsWith("/") ? repository : repository + "/";
                String version = split[2];
                String fileVersion;
                String[] s = version.split("@", 2);
                if (s.length > 1) {
                    version = s[0];
                    fileVersion = s[1];
                } else {
                    fileVersion = version;
                }

                String fileName = split[1] + "-" + fileVersion + (split.length < 4 ? "" : "-" + split[3].replace(":", "-"));
                String url = repo + split[0].replace(".", "/") + "/" + split[1] + "/" + version + "/" + fileName + ".jar";

                if (!getFolder().exists()) {
                    getFolder().mkdirs();
                }
                File file = new File(getFolder(), fileName + ".jar");
                return file.exists() ? file : download(url, file);
            }
        };
    }

    public void load() {
        for (Dependency dependency : Dependency.VALUES) {
            try {
                String name = MessageFormat.format(dependency.test, (Object[]) dependency.getRelocation().to);
                if (dependency.isInner()) {
                    Class.forName(name, true, ezlib.getClassLoader());
                } else {
                    Class.forName(name);
                }
            } catch (ClassNotFoundException e) {
                plugin.getLogger().info("Loading dependency " + dependency.path);
                ezlib.load(dependency.path, dependency.repository, dependency.getRelocationMap(), !dependency.inner);
            }
        }
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public Ezlib getEzlib() {
        return ezlib;
    }

    public ClassLoader getClassLoader() {
        return getEzlib().getClassLoader();
    }

    public void close()  {
        ezlib.close();
    }

    private static String alias(String s) {
        return s.replace(DOT_ALIAS, ".").replace(PACKAGE_ALIAS, LibraryLoader.class.getPackage().getName());
    }

    public static final class Version {

        public static final String BSTATS = "3.0.0";
        public static final String EXAMINATION = "1.3.0";
        public static final String ADVENTURE = "4.11.0";
        public static final String ADVENTURE_PLATFORM = "4.1.2";
        public static final String MSG = "2.2.4-SNAPSHOT@2.2.4-20210406.012549-1";
        public static final String CONFIGURATE = "4.1.2";

    }

    public enum Dependency {

        // BStats API
        BSTATS_BASE(
                "{0}.MetricsBase",
                "org{}bstats:bstats-base:" + Version.BSTATS,
                Relocation.of("org{}bstats", "{package}.lib.bstats")
        ),
        BSTATS(
                "{0}.bukkit.Metrics",
                "org{}bstats:bstats-bukkit:" + Version.BSTATS,
                BSTATS_BASE.relocation
        ),
        // Caffeine
        CAFFEINE(
                "{0}.Cache",
                "com{}github{}ben-manes{}caffeine:caffeine:3.1.1",
                Relocation.of("com{}github{}benmanes{}caffeine{}cache", "{package}.lib.caffeine")
        ),
        // Adventure
        ANNOTATIONS(
                "{0}.NotNull",
                "org{}jetbrains:annotations:23.0.0",
                Relocation.of("org{}jetbrains{}annotations", "{package}.lib.annotations",
                        "org{}intellij{}lang{}annotations", "{package}.lib.annotations.lang")
        ),
        EXAMINATION_API(
                "{1}.Examinable",
                "net{}kyori:examination-api:" + Version.EXAMINATION,
                Relocation.of("net{}kyori{}adventure", "{package}.lib.adventure"),
                Relocation.of("net{}kyori{}examination", "{package}.lib.examination"),
                ANNOTATIONS.relocation
        ),
        EXAMINATION_STRING(
                "{1}.string.StringExaminer",
                "net{}kyori:examination-string:" + Version.EXAMINATION,
                EXAMINATION_API.relocation
        ),
        ADVENTURE_API(
                "{0}.Adventure",
                "net{}kyori:adventure-api:" + Version.ADVENTURE,
                EXAMINATION_API.relocation
        ),
        ADVENTURE_KEY(
                "{0}.key.Key",
                "net{}kyori:adventure-key:" + Version.ADVENTURE,
                ADVENTURE_API.relocation
        ),
        ADVENTURE_NBT(
                "{0}.nbt.BinaryTag",
                "net{}kyori:adventure-nbt:" + Version.ADVENTURE,
                ADVENTURE_API.relocation
        ),
        ADVENTURE_TEXT_MINIMESSAGE(
                "{0}.text.minimessage.MiniMessage",
                "net{}kyori:adventure-text-minimessage:" + Version.ADVENTURE,
                ADVENTURE_API.relocation
        ),
        ADVENTURE_TEXT_SERIALIZER_GSON(
                "{0}.text.serializer.gson.KeySerializer",
                "net{}kyori:adventure-text-serializer-gson:" + Version.ADVENTURE,
                ADVENTURE_API.relocation
        ),
        ADVENTURE_TEXT_SERIALIZER_GSON_LEGACY_IMPL(
                "{0}.text.serializer.gson.legacyimpl.NBTLegacyHoverEventSerializer",
                "net{}kyori:adventure-text-serializer-gson-legacy-impl:" + Version.ADVENTURE,
                ADVENTURE_API.relocation
        ),
        ADVENTURE_TEXT_SERIALIZER_LEGACY(
                "{0}.text.serializer.legacy.LegacyFormat",
                "net{}kyori:adventure-text-serializer-legacy:" + Version.ADVENTURE,
                ADVENTURE_API.relocation
        ),
        ADVENTURE_TEXT_SERIALIZER_BUNGEECORD(
                "{0}.text.serializer.bungeecord.BungeeComponentSerializer",
                "net{}kyori:adventure-text-serializer-bungeecord:" + Version.ADVENTURE_PLATFORM,
                ADVENTURE_API.relocation
        ),
        ADVENTURE_PLATFORM_API(
                "{0}.platform.AudienceProvider",
                "net{}kyori:adventure-platform-api:" + Version.ADVENTURE_PLATFORM,
                ADVENTURE_API.relocation
        ),
        ADVENTURE_PLATFORM_FACET(
                "{0}.platform.facet.FacedAudienceProvider",
                "net{}kyori:adventure-platform-facet:" + Version.ADVENTURE_PLATFORM,
                ADVENTURE_API.relocation
        ),
        ADVENTURE_PLATFORM_BUKKIT(
                "{0}.platform.bukkit.BukkitAudience",
                "net{}kyori:adventure-platform-bukkit:" + Version.ADVENTURE_PLATFORM,
                ADVENTURE_API.relocation
        ),
        // Msg
        MSG_COMMON(
                "{0}.Extension",
                "me{}mattstudios:triumph-msg-commonmark:" + Version.MSG,
                "https://repo.triumphteam.dev/snapshots/",
                Relocation.of("me{}mattstudios{}msg", "{package}.lib.msg"),
                ADVENTURE_API.relocation
        ),
        MSG_CORE(
                "{0}.MessageOptions",
                "me{}mattstudios:triumph-msg-core:" + Version.MSG,
                MSG_COMMON.repository,
                MSG_COMMON.relocation
        ),
        MSG_ADVENTURE(
                "{0}.AdventureMessage",
                "me{}mattstudios:triumph-msg-adventure:" + Version.MSG,
                MSG_COMMON.repository,
                MSG_COMMON.relocation
        ),
        // Acf
        ACF(
                "{0}.commands.ACFUtil",
                "co{}aikar:acf-bukkit:0.5.1-SNAPSHOT@0.5.1-20220801.070114-17",
                "https://repo.aikar.co/content/groups/aikar/",
                Relocation.of("co{}aikar{}commands", "{package}.lib.acf.commands",
                        "co{}aikar{}locales", "{package}.lib.acf.locales")
        ),
        // Mysql driver
        PROTOBUF(
                "{0}.Api",
                "com{}google{}protobuf:protobuf-java:3.19.4",
                Relocation.of("com{}google{}protobuf", "{package}.lib.protobuf")
        ),
        MYSQL_DRIVER(
                "{0}.cj.MysqlConnection",
                "mysql:mysql-connector-java:8.0.30",
                Relocation.of("com{}mysql", "{package}.lib.mysql"),
                PROTOBUF.relocation
        ),
        // Jedis
        GSON(
                "{0}.Gson",
                "com{}google{}code{}gson:gson:2.8.9",
                Relocation.of("com{}google{}gson", "{package}.lib.gson")),
        COMMONS_POOL2(
                "{0}.pool2.ObjectPool",
                "org{}apache{}commons:commons-pool2:2.11.1",
                Relocation.of("org{}apache{}commons", "{package}.lib.commons")
        ),
        JSON(
                "{0}.JSONObject",
                "org{}json:json:20211205",
                Relocation.of("org{}json", "{package}.lib.json")
        ),
        SLF4J_API(
                "{0}.Logger",
                "org{}slf4j:slf4j-api:1.7.32",
                Relocation.of("org{}slf4j", "{package}.lib.slf4j")
        ),
        SLF4J_NOP(
                "{0}.impl.StaticLoggerBinder",
                "org{}slf4j:slf4j-nop:1.7.32",
                Relocation.of("org{}slf4j", "{package}.lib.slf4j")
        ),
        JEDIS(
                "{0}.Jedis",
                "redis{}clients:jedis:4.2.3",
                Relocation.of("redis{}clients{}jedis", "{package}.lib.jedis"),
                GSON.relocation,
                COMMONS_POOL2.relocation,
                JSON.relocation,
                SLF4J_API.relocation
        ),
        // Hikari
        HIKARI(
                "{0}.HikariConfig",
                "com{}zaxxer:HikariCP:5.0.1",
                Relocation.of("com{}zaxxer{}hikari", "{package}.lib.hikari"),
                SLF4J_API.relocation
        ),
        // Configurate
        GEANTYREF(
                "{0}.CaptureType",
                "io{}leangen{}geantyref:geantyref:1.3.11",
                Relocation.of("io{}leangen{}geantyref", "{package}.lib.geantyref")
        ),
        CHECKER(
                "{0}.framework.qual.LiteralKind",
                "org{}checkerframework:checker-qual:3.22.0",
                Relocation.of("org{}checkerframework", "{package}.lib.checkerframework")
        ),
        CONFIGURATE_CORE(
                "{0}.ConfigurationNode",
                "org{}spongepowered:configurate-core:" + Version.CONFIGURATE,
                Relocation.of("org{}spongepowered{}configurate", "{package}.lib.configurate"),
                GEANTYREF.relocation,
                CHECKER.relocation
        ),
        CONFIGURATE_GSON(
                "{0}.gson.GsonVisitor",
                "org{}spongepowered:configurate-gson:" + Version.CONFIGURATE,
                CONFIGURATE_CORE.relocation
        ),
        // LocaleLib
        LOCALE_LIB(
                "{0}.LocaleManager",
                "com{}github{}PikaMug:LocaleLib:2.8",
                "https://jitpack.io/",
                Relocation.of("me{}pikamug{}localelib", "{package}.lib.localelib")
        );

        public static final Dependency[] VALUES = values();

        private final String test;
        private final String path;
        private final String repository;
        private final boolean inner;
        private final Relocation relocation;

        private Map<String, String> relocationMap;

        Dependency(String test, String path, Relocation... relocations) {
            this(test, path, MAVEN_REPOSITORY, relocations);
        }

        Dependency(String test, String path, String repository, Relocation... relocations) {
            this(test, path, repository, false, relocations);
        }

        Dependency(String test, String path, boolean inner, Relocation... relocations) {
            this(test, path, MAVEN_REPOSITORY, inner, relocations);
        }

        Dependency(String test, String path, String repository, boolean inner, Relocation... relocations) {
            this.test = alias(test);
            this.path = alias(path);
            this.repository = repository;
            this.inner = inner;
            if (relocations.length == 1) {
                this.relocation = relocations[0];
                return;
            } else if (relocations.length == 0) {
                this.relocation = Relocation.of();
                return;
            }

            int size = 0;
            for (Relocation relocation : relocations) {
                size = size + relocation.size();
            }
            if (size < 1) {
                this.relocation = Relocation.of();
                return;
            }

            String[] from = new String[size];
            String[] to = new String[size];
            for (int i = 0; i < size; i++) {
                for (Relocation relocation : relocations) {
                    for (int i1 = 0; i1 < relocation.size(); i1++) {
                        from[i] = relocation.from[i1];
                        to[i] = relocation.to[i1];
                        i++;
                    }
                }
            }
            this.relocation = new Relocation(from, to);
        }

        public String getTest() {
            return test;
        }

        public String getPath() {
            return path;
        }

        public String getRepository() {
            return repository;
        }

        public Relocation getRelocation() {
            return relocation;
        }

        public Map<String, String> getRelocationMap() {
            if (relocationMap == null) {
                Map<String, String> map = new HashMap<>();
                for (int i = 0; i < relocation.size(); i++) {
                    String key = alias(relocation.getFrom()[i]);
                    String value = alias(relocation.getTo()[i]);
                    map.put(key, value);
                }
                relocationMap = map;
            }
            return relocationMap;
        }

        public boolean isInner() {
            return inner;
        }
    }

    public static final class Relocation {

        private final String[] from;
        private final String[] to;

        public static Relocation of() {
            return new Relocation(new String[0], new String[0]);
        }

        public static Relocation of(String from, String to) {
            return new Relocation(new String[] {alias(from)}, new String[] {alias(to)});
        }

        public static Relocation of(String from1, String to1, String from2, String to2) {
            return new Relocation(new String[] {alias(from1), alias(from2)}, new String[] {alias(to1), alias(to2)});
        }

        public static Relocation of(String from1, String to1, String from2, String to2, String from3, String to3) {
            return new Relocation(new String[] {alias(from1), alias(from2), alias(from3)}, new String[] {alias(to1), alias(to2), alias(to3)});
        }

        public static Relocation of(String from1, String to1, String from2, String to2, String from3, String to3, String from4, String to4) {
            return new Relocation(new String[] {alias(from1), alias(from2), alias(from3), alias(from4)}, new String[] {alias(to1), alias(to2), alias(to3), alias(to4)});
        }

        public Relocation(String[] from, String[] to) {
            this.from = from;
            this.to = to;
        }

        public String[] getFrom() {
            return from;
        }

        public String[] getTo() {
            return to;
        }

        public int size() {
            return from.length;
        }
    }
}
