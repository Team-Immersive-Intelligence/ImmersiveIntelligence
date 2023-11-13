### On Javadoc Comment Standards
#### by Pabilo8, 28.10.2023

#### About the Standard  
II is meant to be compliant with Oracle's standard JavaDoc, but also uses following these non-standard tags:  
- `@updated`
- `@ii-approved`
Additionally, the `@since` tag is used with a date instead of a project version.

#### Comment Header
Below is an example template for a II javadoc comment header.   

```java
/**
 * @author Jan Kowalski (kowalski@iiteam.net)
 * @since 1.05.2019
 * @updated 8.04.2020
**/
```
All such comments should contain:
- an `@author` tag with the author's name, f.e. `@author Jan Kowalski` 
  - this tag can contain an email address `@author Jan Kowalski (kowalski@iiteam.net)` (optional for Foreign Contributors, necessary for Internal Contributors)
- a `@since` tag with the date of in format DD.MM.YYYY, where month has to be 2 digit
- a `@updated` tag with the date of in format DD.MM.YYYY, where month has to be 2 digit

After a review and approval by the Project Author (Pabilo8) and Project Documentation Specialist (GabrielV), the comment will be assigned an additional `@ii-approved` tag followed by the current version number.
```java
/**
 * @ii-approved 0.3.0
 * @author Jan Kowalski (kowalski@iiteam.net)
 * @since 1.05.2019
 * @updated 8.04.2020
 */
```

#### Comment Body


#### Links, Code Blocks and References
To present a fragment of code use a combination of `<pre>{@code ...}</pre>`, this will assure proper multiline formatting.
For example:
```java
/**
 * Checking initialization
 * <pre>{@code
 * Device device = new Device(deviceProvider);
 * assertTrue(device.isWorking());
 * } </pre>
 */
```
To refer to another class or its method, use `{@link Clazz}`.
```java
/**
 * BreakSpeed is fired when a player attempts to harvest a block.<br>
 * This event is fired whenever a player attempts to harvest a block in
 * {@link EntityPlayer#canHarvestBlock(IBlockState)}.<br>
 **/
```

#### Example Comment
```java
/**
 * @ii-approved 0.3.0
 * @author Yuriy Stakanov (stakanov@omsk.su)
 * @author Jan Kowalski (kowalski@iiteam.net)
 * @since 1.05.1947
 * @updated 8.04.2020
 * 
 * <strong><em>Ingeniator</em></strong> est qui <strong>difficultates</strong> solvit, <em>dolorem et laborem</em> <strong>aliorum</strong> hominum levans.
 * <strong><em>Engineer</em></strong> is a person who solves <strong>problems</strong>, relieving <strong>others</strong> around of <em>the hardships</em>.
 * <strong><em>Inżynier</em></strong> jest to człowiek, który rozwiązując <strong>problemy</strong>, łagodzi <em>ciężar pracy</em> <strong>innych</strong>.
 * <strong><em>Ein Ingenieur</em></strong> ist wer durch <strong>Problemlösung</strong>, die <em>Schwierigkeiten</em> von <strong>Anderes</strong> abnimmt.
 * <strong><em>Інженер</em></strong> це людина яка вирішує <strong>проблеми</strong>, позбавляючи <strong>інших</strong> від <em>труднощів</em>.
 * <strong><em>Інженер</em></strong> это человек который решает <strong>проблемы</strong>, избавляя <strong>других</strong> от <em>Сложностей</em>
 * <strong><em>Inženýr</em></strong> je osoba která řeší problémy, a zbavuje ostatní od útrap.
 */
public class Engineer extends Person
{
	
}
```