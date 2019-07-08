# Changelog
All notable changes to this project will be documented in this file.

## [1.8.8-2.0.0.17] - 2019-07-08
### Changed
- Backported the 1.12.2 version
- See for changes [here](https://github.com/MC-U-Team/Music-Player/blob/1.12.2/CHANGELOG.md).
- Close streams in dependencies correctly with try with resources
- Fix UrlStreamHandler is already defined
- Fix crash when skipping backwards and no song is played
- No need for jna and jna-platform repacked as in 1.8 because this is now shiped with minecraft