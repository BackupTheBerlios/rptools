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
    spec="1.5+" codebase="<? echo $self ?>">
	<information>
		<title>RPTools MapTool</title>
		<vendor><? echo $base ?></vendor>
		<offline-allowed/>
	</information>
	
	<security>
		<all-permissions />
	</security>
	
	<resources>
		<j2se version="1.5+" java-vm-args="-Xms64m -Xmx128m"/>
		
		<jar href="maptool-1.0M2.b11.jar" />
		<jar href="lib/clientserver-1.0.b6.jar" />		
		<jar href="lib/hessian-2.1.12.jar" />		
		<jar href="lib/looks-1.3b1.jar" />		
		<jar href="lib/withay-util.jar" />		
	</resources>
	
	<application-desc main-class="net.rptools.maptool.client.MapToolClient" />
</jnlp>
