# Changelog
All notable changes to this project will be documented in this file.

## [1.14.4-2.1.1.220] - 2023-07-25
### Changed
 - Fix crash on startup when twitch.tv and other provider sites cannot be reached

## [1.14.4-2.1.1.209] - 2023-06-14
### Changed
 - Update lavaplayer to 1.4.2
 - Update buildscripts

## [1.14.4-2.1.1.198] - 2023-03-09
### Changed
 - Update lavaplayer to 1.4.0

## [1.14.4-2.1.1.190] - 2023-01-07
### Changed
 - Update lavaplayer to 1.3.99.2

## [1.14.4-2.1.1.181] - 2022-09-14
### Changed
 - Update lavaplayer to 1.3.98.4
 - Update required uteamcore

## [1.14.4-2.1.1.174] - 2022-08-12
### Changed
 - Update lavaplayer to 1.3.98.3
 - Fix some possible crashes

## [1.14.4-2.1.1.164] - 2022-07-09
### Changed
 - Update lavaplayer to 1.3.98.1
 - Include arm natives
 - Fix some crashes

## [1.14.4-2.1.1.155] - 2022-06-20
### Changed
 - Update lavaplayer to 1.3.97.1
 - Shade some dependencies and transform tinyfd java wrapper at runtime to fix some native loading issues.

## [1.14.4-2.1.1.145] - 2022-04-07
### Changed
 - Update lavaplayer (lavaplayer-fork by Walkyst) to 1.3.97

## [1.14.4-2.1.1.138] - 2021-06-29
### Changed
 - Update lavaplayer to 1.3.78

## [1.14.4-2.1.1.133] - 2021-06-09
### Changed
 - Update lavaplayer to 1.3.77

## [1.14.4-2.1.1.126] - 2021-04-10
### Changed
 - Update lavaplayer to 1.3.76

## [1.14.4-2.1.1.120] - 2021-03-08
### Changed
 - Update lavaplayer to 1.3.73

## [1.14.4-2.1.1.114] - 2021-01-16
### Changed
 - Update lavaplayer to 1.3.66

## [1.14.4-2.1.1.109] - 2020-11-23
### Changed
 - Update lavaplayer to 1.3.61

## [1.14.4-2.1.1.103] - 2020-11-08
### Changed
 - Update lavaplayer to 1.3.59

## [1.14.4-2.1.1.98] - 2020-11-07
### Changed
 - Update lavaplayer to 1.3.58

## [1.14.4-2.1.1.93] - 2020-10-17
### Changed
 - Update lavaplayer to 1.3.58 (unofficial fork of Devoxin) to fix some youtube issues
 - Update the dependency loader to support jdk9+

## [1.14.4-2.1.0.86] - 2020-07-23
### Changed
 - Update lavaplayer to 1.3.49

## [1.14.4-2.0.10.78] - 2020-06-10
### Changed
 - Update lavaplayer to 1.3.49

## [1.14.4-2.0.10.72] - 2020-04-13
### Changed
 - Backport some fixes

## [1.14.4-2.0.9.65] - 2020-04-10
### Changed
 - Update lavaplayer to 1.3.46
 - Fixed [#42](https://github.com/MC-U-Team/Music-Player/issues/42)

## [1.14.4-2.0.9.60] - 2020-02-10
### Changed
 - Fixed [#33](https://github.com/MC-U-Team/Music-Player/issues/33) by using utf-8 as standard charset
 - Fixed [#35](https://github.com/MC-U-Team/Music-Player/issues/35)
 - Fixed [#36](https://github.com/MC-U-Team/Music-Player/issues/36)
 - Fixed [#37](https://github.com/MC-U-Team/Music-Player/issues/37)
 - Fixed [#38](https://github.com/MC-U-Team/Music-Player/issues/38)
 - Rewritten many parts of the playlist class that handles the next song selection
 - Fixed many potential npe
 - When a song cannot be loaded the playlist now skips the errored song instead of stopping the playlist

## [1.14.4-2.0.8.57] - 2020-02-07
### Changed
 - Updated lavaplayer to 1.3.34
 - Updated uteamcore to 2.8.2.150
 - Updated forge to 28.2.0

## [1.14.4-2.0.7.50] - 2020-01-15
### Changed
 - Updated lavaplayer to 1.3.33

## [1.14.4-2.0.6.47] - 2019-12-28
### Changed
 - Fixed some scrolling and mouse release issues with the children gui system in minecraft. This fixed the volume slider not releasing the drag and the scrol bug in the playlists
 - Fixed [#21](https://github.com/MC-U-Team/Music-Player/issues/21)

## [1.14.4-2.0.5.46] - 2019-12-10
### Added
 - Added support for voice chat mod internally

## [1.14.4-2.0.4.44] - 2019-12-05
### Changed
 - Fixed sondcloud not working anymore with updating lavaplayer to 1.3.32
 - Fixed [#29](https://github.com/MC-U-Team/Music-Player/issues/29)
 
### Added
 - Add PGA support (Thanks ArtixAllMighty [#28](https://github.com/MC-U-Team/Music-Player/pull/28))

## [1.14.4-2.0.3.43] - 2019-09-11
### Changed
 - Fixes [#22](https://github.com/MC-U-Team/Music-Player/issues/22) and [#23](https://github.com/MC-U-Team/Music-Player/issues/23) with updating lavaplayer to 1.3.22
 - Update required forge version to 28.0.100

## [1.14.4-2.0.3.35] - 2019-08-15
### Changed
 - Fixes [#19](https://github.com/MC-U-Team/Music-Player/issues/19)

## [1.14.4-2.0.2.30] - 2019-08-09
### Changed
 - Fixed crash because of forge update (28.0.45) [#15](https://github.com/MC-U-Team/Music-Player/issues/15)

## [1.14.4-2.0.2.29] - 2019-08-03
### Changed
 - Fixed youtube provider not working. Updated lavaplayer which fixed [#14](https://github.com/MC-U-Team/Music-Player/issues/14)

## [1.14.4-2.0.1.21] - 2019-07-23
### Changed
 - Update to 1.14.4
