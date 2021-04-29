package dev.spaceseries.spacechat.internal.dependency.relocation;

import org.objectweb.asm.commons.Remapper;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Remaps class names and types using defined {@link Relocation} rules. */
final class RelocatingRemapper extends Remapper {
  private static final Pattern CLASS_PATTERN = Pattern.compile("(\\[*)?L(.+);");

  private final Collection<Relocation> rules;

  /**
   * Instantiates a new Relocating remapper.
   *
   * @param rules the rules
   */
  RelocatingRemapper(final Collection<Relocation> rules) {
    this.rules = rules;
  }

  @Override
  public Object mapValue(final Object object) {
    if (object instanceof String) {
      final String relocatedName = relocate((String) object, true);
      if (relocatedName != null) {
        return relocatedName;
      }
    }
    return super.mapValue(object);
  }

  @Override
  public String map(final String name) {
    final String relocatedName = relocate(name, false);
    if (relocatedName != null) {
      return relocatedName;
    }
    return super.map(name);
  }

  private String relocate(String name, final boolean isClass) {
    String prefix = "";
    String suffix = "";

    final Matcher m = CLASS_PATTERN.matcher(name);
    if (m.matches()) {
      prefix = String.format("%sL", m.group(1));
      suffix = ";";
      name = m.group(2);
    }

    for (final Relocation r : rules) {
      if (isClass && r.canRelocateClass(name)) {
        return prefix + r.relocateClass(name) + suffix;
      } else if (r.canRelocatePath(name)) {
        return prefix + r.relocatePath(name) + suffix;
      }
    }

    return null;
  }
}