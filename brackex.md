# Documentation of the Custom Bracketed Expression Engine ("brackex")

This engine uses a non-standard syntax based on specific symbols to define character sets, quantifiers, groups, alternatives, and classes. These expressions are called **Bracketed Expressions**—or **brackex** for short. The following documentation describes the syntax as designed.

---

## 1. Character Sets

Character sets allow you to define, for a given position in the string, an allowed or disallowed set of characters.

### 1.1. Simple Set

- **Syntax:** `「…」`  
- **Example:** `「abced0123」`  
- **Description:**  
  All characters listed between `「` and `」` form the allowed set for that position.

### 1.2. Range

- **Syntax:** `「char1〜char2」`  
- **Example:** `「a〜c」`  
- **Description:**  
  The symbol `〜` is used to define a range. In this example, all characters between `a` and `c` (inclusive) are accepted.

### 1.3. Negative Set

- **Syntax:** `「〤…」`  
- **Example:** `「〤a〜c」`  
- **Description:**  
  The prefix `〤` indicates that the set is negative. The expression `「〤a〜c」` means that any character **except** those between `a` and `c` is accepted.

### 1.4. Combination of Elements

- **Example:** `「〤abcedf0〜9」`  
- **Description:**  
  This negative set combines explicit characters (`a`, `b`, `c`, `e`, `d`, `f`) with a range (`0〜9`). It accepts any character **not present** in this combined set.

---

## 2. Quantifiers

Quantifiers determine the number of occurrences a given pattern must have. In this syntax, the quantifier symbol `〇` precedes the pattern to which it applies.

### 2.1. Basic Quantifiers

- **`〇?pattern`**  
  - **Example:** `〇?a`  
  - **Description:** The pattern `a` is optional (0 or 1 occurrence).

- **`〇*pattern`**  
  - **Example:** `〇*a`  
  - **Description:** The pattern `a` may appear zero or more times.

- **`〇+pattern`**  
  - **Example:** `〇+a`  
  - **Description:** The pattern `a` must appear at least once (1 or more occurrences).

### 2.2. Numeric Quantifiers

- **`〇{n}pattern`**  
  - **Example:** `〇{2}a`  
  - **Description:** The pattern `a` must appear exactly *n* times (here, 2 times).

- **`〇{m-n}pattern`**  
  - **Example:** `〇{1-3}a`  
  - **Description:** The pattern `a` must appear between *m* and *n* times (here, between 1 and 3 times).

- **`〇{m,n}pattern`**  
  - **Example:** `〇{2,4}a`  
  - **Description:** The pattern `a` must appear between *m* and *n* times (here, between 2 and 4 times).

> **Note:**  
> Unlike traditional regex, where the quantifier follows the pattern, in this syntax the quantifier precedes the pattern.

---

## 3. Groups

Groups allow you to combine patterns either to apply quantifiers collectively or to capture a portion of the match.

### 3.1. Unnamed Groups

- **Syntax:** `〘 … 〙`  
- **Example:** `a〘〇+b〙c`  
- **Description:**  
  The pattern `〇+b` is grouped without a name, enabling collective capturing or processing.

### 3.2. Named Groups

- **Syntax:** `〘name〡pattern〙`  
- **Example:** `a〘group1〡〇+b〙c`  
- **Description:**  
  The group is identified by a name (here, `group1`), which facilitates its reuse or extraction during further processing.

---

## 4. Logical Alternatives

Alternatives allow you to specify multiple possible patterns for a given position.

- **Syntax:** `【 alternative1 〡 alternative2 〡 … 】`  
- **Examples:**
  - `a【<〡/〡\】c`  
    - **Description:** Between `a` and `c`, one of the following patterns must be matched: `<`, `/`, or `\`.
  - `a【〇{1-2}<〡〇{1-2}/〡〇{1-2}\】c`  
    - **Description:** In this case, each alternative must appear between 1 and 2 times.

> **Note:**  
> The separator `〡` is used to delimit the different alternatives within the `【` and `】` brackets. Please note that `〡` is a distinct character from the standard vertical bar `|`, even though they look quite similar.

---

## 5. Predefined Classes

Predefined classes let you reference frequently used sets of characters using the symbol `〴` followed by an identifier.

### 5.1. Standard Classes

- **`〴w`**  
  *Description:* Corresponds to "word" characters (letters, digits, and underscore), similar to `\w` in standard regex.

- **`〴d`**  
  *Description:* Corresponds to digits, equivalent to `\d`.

- **`〴s`**  
  *Description:* Corresponds to whitespace characters, similar to `\s`.

### 5.2. Literal Classes

- **`〴'`**  
  *Description:* Represents any single quote character in Unicode. This includes the ASCII apostrophe (`'`) as well as typographic variants such as `‘`, `’`, and others.

- **`〴"`**  
  *Description:* Represents any double quote character in Unicode. This includes the ASCII double quote (`"`) as well as typographic variants such as `“`, `”`, and others.

### 5.3. Alphanumeric Classes and Extended Variants

- **`〴an`**  
  *Description:* Denotes the set of alphanumeric characters (letters and digits) as defined by Unicode. This definition is much broader than that of `〴w` or `〴d`, which remain limited to the basic ASCII character set.

- **`〴_an`**  
  *Description:* Includes alphanumeric characters plus the underscore (`_`).

- **`〴_.an`**  
  *Description:* Allows alphanumeric characters, the underscore (`_`), and the period (`.`).
  
- **`〴_-an`**  
  *Description:* Allows alphanumeric characters, the underscore (`_`), and the hyphen (`-`).
  
- **`〴_-.an`**  
  *Description:* Allows alphanumeric characters, the underscore (`_`), the hyphen (`-`), and the period (`.`).

> **Note:**  
> For `w`, `d`, and `s`, the meaning is identical to that in traditional regular expressions.

---

## 6. General Considerations

### Use of Unicode U+3000 Separators

A key design choice in the brackex syntax is the use of separator characters from the Unicode block [U+3000 CJK Symbols and Punctuation](https://en.wikipedia.org/wiki/CJK_Symbols_and_Punctuation). Although this may require occasional copy-pasting of these symbols, it greatly enhances the readability of expressions by eliminating the need to escape standard ASCII characters. This approach minimizes visual clutter and makes the expressions more intuitive. For reference, please see the summary of symbols used below:

### Summary of Used Symbols and Delimiters

| Symbol/Delimiter  | Usage                                                   |
| ----------------- | --------------------------------------------------------|
| `「 … 」`         | Delimits a set of characters or a range.                |
| `〤`               | Prefix indicating negation in a set.                    |
| `〜`               | Separates the beginning and end of a character range.   |
| `〇`               | Introduces a quantifier (optional, *, +, {…}).           |
| `〘 … 〙`         | Delimits a group (unnamed by default).                  |
| `〘 name 〡 … 〙`   | Delimits a named group (where `name` is the group name). |
| `【 … 】`         | Delimits a set of alternatives.                         |
| `〡`               | Separates alternatives within an alternative set.       |
| `〴`               | Introduces a predefined class.                          |

- **Order of Elements:**  
  Quantifiers, introduced by `〇`, directly precede the pattern they apply to, which is contrary to the convention in traditional regex.

- **Use of Delimiters:**  
  This syntax relies on specific delimiters (derived from East Asian typography) to clearly define the structure of an expression (such as sets, groups, and alternatives).

- **Extensions and Evolution:**  
  This documentation describes the syntax as currently implemented. Adjustments or extensions may be introduced based on evolving needs and user feedback.

---
