<?php
header("Content-type: application/x-java-jnlp-file");
if ( $_REQUEST['debug'] ) {
    header("Content-type: text/plain");
}
$bits = pathinfo( $_SERVER['REQUEST_URI'] );
$base = sprintf("http://%s/",  $_SERVER['SERVER_NAME']);
$self = sprintf("http://%s%s", $_SERVER['SERVER_NAME'],$bits['dirname']);
echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
?>
<jnlp
  spec="1.5+"
  codebase="<? echo $self ?>">
  <information>
    <title>RPTools DiceTool</title>
    <vendor><?echo $base ?></vendor>
  </information>

  <security>
      <all-permissions/>
  </security>

  <resources>
    <j2se version="1.5+" java-vm-args="-Xms64m -Xmx128m" />

    <jar href="dicetool.jar"/>

  </resources>

  <application-desc main-class="com.jcuz.dnd.dmtool.DMTool"/>
</jnlp> 
