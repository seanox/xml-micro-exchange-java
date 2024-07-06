# Configuration

With focus on the use of Seanox XMEX in containers, the configuration is
optimized for the use of environment variables.

TODO:

## Logging

TODO:

### Application

For logging, the __stdout__ is used. Configuration via an environment variable
is not provided. To redirect and forward application logs, the start of the
program should be combined with _pipes_ and commands such as _tee_. 

Simple redirect:

```
java -jar seanox-xmex-0.0.0.0.jar >> logfile.log
```

Simple forwarding:

```
java -jar seanox-xmex-0.0.0.0.jar | tee -a logfile.log
```

Or a forwarding with more dynamics in the file name:

```
java -jar seanox-xmex-0.0.0.0.jar | tee -a logfile_$(date '+%Y%m%d').log
```

### Access Log

By default, the access log is deactivated and must be consciously activated.

TODO:

<table>
  <thead>
    <tr>
      <th>Environment Variable</th>
      <th>Target in application.properties</th>
      <th>Description</th>
    </tr>  
  </thead>
  <tbody>
    <tr>
      <td>
        <code>XMEX_SERVER_ACCESS_LOG</code>
      </td>
      <td>
        <code>server.tomcat.accesslog.enabled</code>
      </td>
      <td>
        TODO:
      </td>
    </tr>
    <tr>
      <td>
        <code>XMEX_SERVER_ACCESS_LOG_PATTERN</code>
      </td>
      <td>
        <code>server.tomcat.accesslog.pattern</code>
      </td>
      <td>
        TODO:
        https://tomcat.apache.org/tomcat-9.0-doc/config/valve.html
      </td>
    </tr>
    <tr>
      <td>
        <code>XMEX_SERVER_ACCESS_LOG_DIRECTORY</code>
      </td>
      <td>
        <code>server.tomcat.accesslog.directory</code>
      </td>
      <td>
        TODO:
        Relative paths are based on the current working directory. If no path is
        specified, the current working directory is used for data output.
      </td>
    </tr>
  </tbody>
</table>
