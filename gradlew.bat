@if "%DEBUG%"=="" @echo off
@rem Gradle startup script for Windows
@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@setlocal

set DEFAULT_JVM_OPTS="-Xmx64m" "-Xms64m"

setlocal enabledelayedexpansion
for /F "usebackq delims=" %%a in ("%GRADLE_USER_HOME%\gradle.properties") do set "%%a"
if defined GRADLE_EXIT_CONSOLE (
  exit /b %GRADLE_EXIT_CONSOLE%
)

set CLASSPATH=%APP_HOME%\gradle\wrapper\gradle-wrapper.jar

"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %GRADLE_OPTS% "-Dorg.gradle.appname=%APP_BASE_NAME%" -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %CMD_LINE_ARGS%

:end
@endlocal & exit /b %ERRORLEVEL%
