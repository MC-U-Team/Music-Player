# Changelog
All notable changes to this project will be documented in this file.

## [1.8.9-2.1.1.259] - 2024-07-12
### Changed
 - Update to custom lavaplayer fork 2.2.1
 - Use new lavalink youtube source manager to fix youtube playpack

## [1.8.9-2.1.1.248] - 2024-04-30
### Changed
 - Update to custom lavaplayer fork 2.1.2.1-SNAPSHOT
 - Fixed [#1022](https://github.com/MC-U-Team/Music-Player/issues/1022)

## [1.8.9-2.1.1.238] - 2024-04-28
### Changed
 - Update to custom lavaplayer fork 2.1.2

## [1.8.9-2.1.1.227] - 2023-08-26
### Changed
 - Update lavaplayer to 1.4.3

## [1.8.9-2.1.1.218] - 2023-07-25
### Changed
 - Fix crash on startup when twitch.tv and other provider sites cannot be reached

## [1.8.9-2.1.1.207] - 2023-06-14
### Changed
 - Update lavaplayer to 1.4.2

## [1.8.9-2.1.1.196] - 2023-03-09
### Changed
 - Update lavaplayer to 1.4.0

## [1.8.9-2.1.1.188] - 2023-01-07
### Changed
 - Update lavaplayer to 1.3.99.2

## [1.8.9-2.1.1.179] - 2022-09-14
### Changed
 - Update lavaplayer to 1.3.98.4

## [1.8.9-2.1.1.172] - 2022-08-12
### Changed
 - Update lavaplayer to 1.3.98.3
 - Fix some possible crashes

## [1.8.9-2.1.1.162] - 2022-07-09
### Changed
 - Update lavaplayer to 1.3.98.1
 - Include arm natives
 - Fix some crashes

## [1.8.9-2.1.1.153] - 2022-06-20
### Changed
 - Update lavaplayer to 1.3.97.1
 - Shade some dependencies and define tinyfd java wrapper at runtime.

## [1.8.9-2.1.1.143] - 2022-04-07
### Changed
 - Update lavaplayer (lavaplayer-fork by Walkyst) to 1.3.97
 - Fix language translation not working bug

## [1.8.9-2.1.1.136] - 2021-06-29
### Changed
 - Update lavaplayer to 1.3.78

## [1.8.9-2.1.1.131] - 2021-06-09
### Changed
 - Update lavaplayer to 1.3.77

## [1.8.9-2.1.1.124] - 2021-04-10
### Changed
 - Update lavaplayer to 1.3.76

## [1.8.9-2.1.1.118] - 2021-03-08
### Changed
 - Update lavaplayer to 1.3.73

## [1.8.9-2.1.1.112] - 2021-01-16
### Changed
 - Update lavaplayer to 1.3.66

## [1.8.9-2.1.1.107] - 2020-11-23
### Changed
 - Update lavaplayer to 1.3.61

## [1.8.9-2.1.1.101] - 2020-11-08
### Changed
 - Update lavaplayer to 1.3.59

## [1.8.9-2.1.1.96] - 2020-11-06
### Changed
 - Update lavaplayer to 1.3.58

## [1.8.9-2.1.1.91] - 2020-10-17
### Changed
 - Update lavaplayer to 1.3.58 (unofficial fork of Devoxin) to fix some youtube issues

## [1.8.9-2.1.0.84] - 2020-07-23
### Changed
 - Update lavaplayer to 1.3.50

## [1.8.9-2.0.3.76] - 2020-06-10
### Changed
 - Update lavaplayer to 1.3.49

## [1.8.9-2.0.3.70] - 2020-04-13
### Changed
 - Update lavaplayer to 1.3.46
 - Backport many other fixes

## [1.8.9-2.0.3.55] - 2020-02-07
 - Update lavaplayer to 1.3.34
 - Fixed [#21](https://github.com/MC-U-Team/Music-Player/issues/21)
 - Fixed [#29](https://github.com/MC-U-Team/Music-Player/issues/29)

## [1.8.9-2.0.2.38] - 2019-09-11
### Changed
 - Fixes [#22](https://github.com/MC-U-Team/Music-Player/issues/22) and [#23](https://github.com/MC-U-Team/Music-Player/issues/23) with updating lavaplayer to 1.3.22

## [1.8.9-2.0.2.24] - 2019-08-03
### Changed
 - Fixes [#14](https://github.com/MC-U-Team/Music-Player/issues/14)

## [1.8.9-2.0.0.18] - 2019-07-08
### Changed
 - Backported the 1.12.2 version
 - See for changes [here](https://github.com/MC-U-Team/Music-Player/blob/1.12.2/CHANGELOG.md).
 - Close streams in dependencies correctly with try with resources
 - Fix UrlStreamHandler is already defined
 - Fix crash when skipping backwards and no song is played
 - No need for jna and jna-platform repacked as in 1.8 because this is now shiped with minecraft
