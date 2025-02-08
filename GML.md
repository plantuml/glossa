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

## Tag Structure in GML

GML tags serve as the fundamental building blocks for representing elements in the Glossa Markup Language. Each tag is designed to encapsulate both the identity of an element (its name) and any related metadata (its attributes). 

### Tag Components

Every GML tag is composed of two main parts:

- **Tag Name:**  
  This is the identifier for the tag, much like an HTML or XML tag name. It tells you what type of element or construct is being represented (for example, `text`, `link`, or `table`).

- **Attributes (Optional):**  
  Attributes are provided as a set of key/value pairs and supply additional information about the element. They follow the tag name within the tag. Each attribute is written in the form:
  
  ```
  key〓value
  ```
  
  Multiple attributes are separated by the field separator (〡).

### Constructing a tag

#### Opening tag with qttributes

An opening tag begins with the start marker, immediately followed by the tag name. If attributes are present, they follow the tag name, each separated by the field separator. Finally, the tag is closed with the end marker. For example:

```
《tagname 〡key1〓value1 〡key2〓value2》
```

In this example:
- `tagname` identifies the element.
- `key1` is assigned the value `value1`.
- `key2` is assigned the value `value2`.

#### Opening tag without attributes

If no attributes are needed, the tag can simply include the tag name between the start and end markers:

```
《tagname 》
```

This format indicates that the element is present without any additional metadata.

### Closing tags

To explicitly denote the end of an element, a closing tag is used. The closing tag mimics the opening tag’s structure but includes a forward slash (`/`) immediately after the start marker to indicate that it is a closing tag:

```
《/tagname 》
```

This is analogous to closing tags in HTML or XML and helps define boundaries for nested or complex structures.

