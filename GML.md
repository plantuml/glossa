# Glossa Markup Language (GML)

Glossa Markup Language (GML) is a pivot format designed to serve as a universal intermediary for converting between various wiki syntaxes. GML leverages four custom characters from the Unicode Private Use Area (PUA) to define its unique syntax elements.

## The Unicode Private Use Area (PUA)

The Unicode Private Use Area (PUA) is a range of code points reserved specifically for custom, application-specific characters. These code points are intentionally left unassigned by the Unicode Consortium, allowing developers to define their own symbols without the risk of conflict with standardized characters.

**Benefits of Using PUA Characters:**

- **Custom Symbols:** Create unique, recognizable symbols tailored to the needs of your language or format.
- **Consistency:** Ensure a consistent and unambiguous syntax by reserving specific code points for GML.
- **Non-Interference:** Avoid conflicts with standard characters, as these code points are not assigned to any typical text.
- **Simplified Encoding:** Unlike HTML or XML for example, there is no need to escape any standard text character, because they do not interfere with those PUA characters.


## GML Special Characters

Below is a table that explains the usage and display of the four special characters used in GML:

| Unicode Code Point | Display Character | Usage Description        |
|--------------------|-------------------|--------------------------|
| **U+E000**         | 《                | Marks the **start of a tag**. |
| **U+E001**         | 》                | Marks the **end of a tag**.   |
| **U+E002**         | 〡                | Acts as a **field separator** within a tag. |
| **U+E003**         | 〓                | Serves as the **assignment operator** within a tag. |

In this documentation, when you see a symbol such as 《, it refers specifically to the Unicode code point **U+E000** from the Private Use Area (PUA).
This is purely a display convention for readability. It is important to understand that this does not represent
the standard CKJ character 《 that exists in Unicode and may be used in GML if needed.

The displayed symbol is simply a visual cue to indicate the designated PUA code point.

