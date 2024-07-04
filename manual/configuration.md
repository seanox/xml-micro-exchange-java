# Configuration

With focus on the use of Seanox XMEX in containers, the configuration is
optimized for the use of environment variables.

TODO:

## Logging

TODO:

### Application

For logging, the __stdout__ is used. Configuration via an environment variable
is not provided. To redirect and forward application logging, the pipe and
commands such as _tee_ should therefore be used.

### Access Log

<table>
  <thead>
    <tr>
      <th>Environment Variable</th>
      <th>application.property</th>
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
        Be careful with relative paths, as these could start in the temp/tmp
        directory of the user. It is better if an absolute path is specified. 
      </td>
    </tr>
  </tbody>
</table>
