<?php
/**
 * Created by PhpStorm.
 * User: Filip
 * Date: 1/21/2016
 * Time: 7:09 PM
 */
require_once("Laptop.php");
require_once("ebay.php");
require_once("shopping.php");
$ebay = new ebay();
$myfile = fopen("id.txt", "w");
$as = array();
for ($i = 1; $i < 101; $i++) {
    $results1 = $ebay->findProduct('findItemsByCategory', '175672', $i);
    $ids = $results1->findItemsByCategoryResponse[0]->searchResult[0];

    $numResults = count($ids->item);

    for ($ii = 0; $ii < $numResults; $ii++) {
        $pom = $ids->item[$ii]->itemId[0];
        if (!in_array($pom, $as)) {
            $as[] = $pom;
        }

        fwrite($myfile, $pom);
        fwrite($myfile, "   ");
    }

}
fclose($myfile);
echo count($as);

