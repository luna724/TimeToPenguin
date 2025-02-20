# Changelogs
## v2.4
- `LunaClient v2`から分離
- AutoMove.kt にて API呼び出しのような機能を追加4
- コードのkotlin化

## v2.0
- `/automove` のみが呼び出された際に発生する `An Unknown Error occurred while attempting to perform this command.` の問題を修正
- `/automove setdirection` が複数の移動方向を指定できるように
- 第1引数に対する補完機能を提供
- 第2引数に対する部分的な補完機能を提供 (`/automove setdirection`に限る)
- `/automove senddatatolunaclient` を追加
- `/automove toggle` がスタートのみを行う問題を修正
- `Robot`モジュールの使用から`KeyBinding.setKeyBindState`の使用に変更
- `/automove setdirection` 移動しないモードを追加
- `/automove setdirection reset` 移動方向のリセット機能を追加
- config `autoMoveDirection`の値タイプを変更 (String -> int)
