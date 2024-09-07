import { exec } from 'child_process';

const javaPath = 'C:/Users/lemue/.jdks/corretto-17.0.12/bin/java.exe';

const classPath = '-cp ./bin/classes'

const className = 'com.domain.compiler.App'

const sourceCodeFile = './resources/source-code.txt'

exec(`${javaPath} ${classPath} ${className} ${sourceCodeFile}`, (err, stdout, _stderr) => {
    if (err) {
        console.error(err);
        return;
    }
    console.log(stdout);
    console.log(_stderr);
});