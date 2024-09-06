import { exec } from 'child_process';

exec('java App Hello', (error, stdout, stderr) => {
  console.log(error)
  console.log(stdout)
  console.log(stderr)
});
