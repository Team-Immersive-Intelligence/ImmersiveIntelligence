### On II In-Game Manual File Standard

#### by Pabilo8, 15.11.2023

#### About the Standard

The standard is based on a limited subset of Markdown syntax for its text formatting.
It is meant to be a portable, editable file that consists of text and embedded objects, which can use *data sources*
providing them with information based on in-game contents.
The files following this standard are to be displayed both in the Immersive Engineering manual and a Web Manual website.

#### Text Formatting

Text formatting is based on a subset of Markdown with some additional tags.

- using `**text**` makes text **bold**
- using `*text*` makes text *italic*
- using `***text***` makes text ***bold and italic***
- using `__text__` makes text __underlined__
- using `[text]` makes text **highlighted** giving it a green tint

#### Links

Currently, it does not support subscript and superscript, but it's planned to be added in the future.

It is possible to add clickable links to other manual entries, including ones not following II's standard.  
This can be done through: `[link text](entry_name)`
Where

- `link text` is the text displayed that will be made clickable
- `page_name` is the text ID used to register the entry in the manual. For II entries it's the filename, with (`page.md`)
  or without the extension (`page`)
  Moreover, if the entry is located in a subfolder it should be included in `entry_name` in form `some/folder/entry_name`.

To create a link to a specific page, a sub-ID (header) or number of the page (in case of entries not following II's standard) is
required.
Such a link has a form `[link text](page_name#subpage)` where
- `subpage` is the sub-ID or page number, as described above

#### Objects

Objects are elements of the page using 