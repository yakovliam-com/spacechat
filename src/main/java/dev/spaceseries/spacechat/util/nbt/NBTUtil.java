package dev.spaceseries.spacechat.util.nbt;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.util.Codec;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.jglrxavpok.hephaistos.nbt.NBT;
import org.jglrxavpok.hephaistos.nbt.NBTException;
import org.jglrxavpok.hephaistos.parser.SNBTParser;

import java.io.StringReader;

public class NBTUtil {

    /**
     * A codec to convert between strings and NBT.
     */
    private static final Codec<NBT, String, NBTException, RuntimeException> NBT_CODEC
            = Codec.of(encoded -> new SNBTParser(new StringReader(encoded)).parse(), NBT::toSNBT);

    /**
     * Returns a BinaryTagHolder from an item stack
     * If it doesn't contain any HoverEvent NBT data, then it returns null
     *
     * @param itemStack item stack
     * @return BTH
     */
    public static @Nullable BinaryTagHolder fromItemStack(ItemStack itemStack) {
        org.jglrxavpok.hephaistos.nbt.NBTCompound compound = compoundFromItemStack(itemStack);
        org.jglrxavpok.hephaistos.nbt.NBTCompound tag = compound.getCompound("tag");

        return (tag != null ? BinaryTagHolder.encode(tag, NBT_CODEC) : null);
    }

    /**
     * Returns a compound from an item stack
     *
     * @param itemStack stack
     * @return compound
     */
    public static org.jglrxavpok.hephaistos.nbt.NBTCompound compoundFromItemStack(ItemStack itemStack) {
        // get nbt from item using NBT-API
        NBTCompound compound = NBTItem.convertItemtoNBT(itemStack);
        // convert to string
        String compoundString = compound.toString();

        // read the compound in, to a {org.jglrxavpok.hephaistos.nbt.NBTCompound} object
        org.jglrxavpok.hephaistos.nbt.NBTCompound nbt;
        try {
            nbt = (org.jglrxavpok.hephaistos.nbt.NBTCompound) new SNBTParser(new StringReader(compoundString)).parse();
        } catch (NBTException ignored) {
            return null;
        }

        return nbt;
    }
}
