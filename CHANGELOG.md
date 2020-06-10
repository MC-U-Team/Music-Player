# Changelog
All notable changes to this project will be documented in this file.

## [1.8.8-2.0.3.75] - 2020-06-10
### Changed
 - Update lavaplayer to 1.3.49

## [1.8.8-2.0.3.69] - 2020-04-13
### Changed
 - Update lavaplayer to 1.3.46
 - Backport many other fixes

## [1.8.8-2.0.3.54] - 2020-02-07
### Changed
 - Update lavaplayer to 1.3.34
 - Fixed [#21](https://github.com/MC-U-Team/Music-Player/issues/21)
 - Fixed [#29](https://github.com/MC-U-Team/Music-Player/issues/29)

## [1.8.8-2.0.2.37] - 2019-09-11
### Changed
 - Fixes [#22](https://github.com/MC-U-Team/Music-Player/issues/22) and [#23](https://github.com/MC-U-Team/Music-Player/issues/23) with updating lavaplayer to 1.3.22

## [1.8.8-2.0.2.23] - 2019-08-03
### Changed
 - Fixes [#14](https://github.com/MC-U-Team/Music-Player/issues/14)

## [1.8.8-2.0.0.17] - 2019-07-08
### Changed
 - Backported the 1.12.2 version
 - See for changes [here](https://github.com/MC-U-Team/Music-Player/blob/1.12.2/CHANGELOG.md).
 - Close streams in dependencies correctly with try with resources
 - Fix UrlStreamHandler is already defined
 - Fix crash when skipping backwards and no song is played
 - No need for jna and jna-platform repacked as in 1.8 because this is now shipped with minecraft
