@echo off
REM #
REM # LivGO项目自动版本发布脚本
REM #
REM
REM 说明：
REM 此脚本用于自动生成正式发布版的版本号。被maven调用执行，在package前/后运行。
REM
REM
REM 注：
REM 脚本使用SubWCRev.exe根据SVN版本信息自动生成网站部署的版本号。
REM 如果TortoiseSVN未安装在缺省目录，为保证开发环境统一，请单独将SubWCRev.exe文件
REM 复制一份放置在C:\Program Files\TortoiseSVN\bin\SubWCRev.exe。
REM


if "%1"=="prepare" goto prepare
if "%1"=="after" goto after

REM -------------------------------------------------------------------
echo #
echo # LivGO项目自动版本发布脚本
echo #
echo=
echo 此脚本用于自动生成正式发布版的版本号。被maven调用执行，在package前/后运行。
echo=
echo 命令：
echo     %0 prepare     - 在mvn prepare-package阶段被调用
echo     %0 after       - 在mvn package阶段打包完成后被调用

goto end


REM -------------------------------------------------------------------
REM 1.在mvn prepare-package阶段调用：
:prepare
cd ../
REM 1.1.将原开发环境中的version.js文件暂存;
copy  .\livgo-manage-front\src\const\Version.vue  .\target-config\Version_dev.vue
REM 1.2.通过SubWCRev生成当前的发布版的版本号,并更新Version.vue文件;
REM 1.2.1
"C:\Program Files\TortoiseSVN\bin\SubWCRev.exe" . .\target-config\version_template.txt .\livgo-manage-front\src\const\Version.vue
REM 1.3.前端代码打包；
cd .\livgo-manage-front
call npm run build
cd ../
REM 1.4.复制到server模块webapp文件夹下
XCOPY .\livgo-manage-front\dist .\livgo-server\src\main\webapp /S /Y
goto end


REM -------------------------------------------------------------------
REM 2.在mvn package阶段打包完成后调用：
:after
cd ../
REM 2.1.恢复1.1步骤所暂存的原开发环境中的version.js文件；
move  .\target-config\Version_dev.vue .\livgo-manage-front\src\const\Version.vue
goto end


REM -------------------------------------------------------------------
:end
@echo on