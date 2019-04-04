<?php
/**
 * Created by PhpStorm.
 * User: Filip
 * Date: 24-Jan-16
 * Time: 9:07 PM
 */
require_once("Laptop.php");
require_once("shopping.php");
$myfileLaptop = fopen("Laptops.txt", "w");

$mydata = fopen("id.txt", "r");
$niz = array();
$data = fread($mydata, filesize("id.txt"));
$niz = explode("  ", $data);

fclose($mydata);
$myfileLaptop = fopen("Laptops.txt", "w");

$ebay = new eBayShopping();

global $uzorak;

for ($item = 0; $item < count($niz) - 1; $item++) {
    $a = $ebay->get('GetSingleItem', ltrim($niz[$item]));
    $specifics = $a->Item->ItemSpecifics->NameValueList;
    $price = $a->Item->ConvertedCurrentPrice->Value;
    $title = $a->Item->Title;
    $id = $a->Item->ItemID;
    $laptop = new Laptop();
    $laptop->setPrice($price);
    $laptop->setId($id);
    if ((substr_count($title, 'SSD') > 0) || (substr_count($title, 'ssd') > 0) || (substr_count($title, 'Ssd') > 0)) {
        $laptop->setSsd("Yes");
    } else {
        $laptop->setSsd("No");
    }

    $skip = false;

    for ($s = 0; $s < count($specifics); $s++) {
        if (strcmp("Operating System", ltrim($specifics[$s]->Name)) === 0) {
            if (strpos(ltrim($specifics[$s]->Value[0]), "Windows XP") !== false) {
                $laptop->setOs("WindowsXP");
            } elseif (strpos(ltrim($specifics[$s]->Value[0]), "Windows 7") !== false) {
                $laptop->setOs("Windows7");
            } elseif (strpos(ltrim($specifics[$s]->Value[0]), "Windows 8") !== false || strpos(ltrim($specifics[$s]->Value[0]), "Windows 8.1") !== false) {
                $laptop->setOs("Windows8");
            } elseif (strpos(ltrim($specifics[$s]->Value[0]), "Windows 10") !== false) {
                $laptop->setOs("Windows10");
            } elseif (strpos(ltrim($specifics[$s]->Value[0]), "Chrome OS") !== false) {
                $laptop->setOs("ChromeOS");
            } else {
                $laptop->setOs("Windows7");
            }

        }
        if (strcmp("Processor Type", ltrim($specifics[$s]->Name)) === 0) {
            if (strpos(ltrim($specifics[$s]->Value[0]), "i3") && strpos(ltrim($specifics[$s]->Name), "4th")) {
                $laptop->setProcessor("i34th");
            } elseif (strpos(ltrim($specifics[$s]->Value[0]), "i3") !== false && strpos(ltrim($specifics[$s]->Value[0]), "3rd") !== false) {
                $laptop->setProcessor("i33rd");
            } elseif (strpos(ltrim($specifics[$s]->Value[0]), "i5") !== false && strpos(ltrim($specifics[$s]->Value[0]), "1st") !== false) {
                $laptop->setProcessor("i51st");
            } elseif (strpos(ltrim($specifics[$s]->Value[0]), "i5") !== false && strpos(ltrim($specifics[$s]->Value[0]), "2nd") !== false) {
                $laptop->setProcessor("i52nd");
            } elseif (strpos(ltrim($specifics[$s]->Value[0]), "i5") !== false && strpos(ltrim($specifics[$s]->Value[0]), "3rd") !== false) {
                $laptop->setProcessor("i53rd");
            } elseif (strpos(ltrim($specifics[$s]->Value[0]), "i5") !== false && strpos(ltrim($specifics[$s]->Value[0]), "4th") !== false) {
                $laptop->setProcessor("i54th");
            } elseif (strpos(ltrim($specifics[$s]->Value[0]), "i7") !== false && strpos(ltrim($specifics[$s]->Value[0]), "3rd") !== false) {
                $laptop->setProcessor("i73rd");
            } elseif (strpos(ltrim($specifics[$s]->Value[0]), "i7") !== false && strpos(ltrim($specifics[$s]->Value[0]), "4th") !== false) {
                $laptop->setProcessor("i74th");
            } elseif (strpos(ltrim($specifics[$s]->Value[0]), "Pentium") !== false) {
                $laptop->setProcessor("Pentium");
            } elseif (strpos(ltrim($specifics[$s]->Value[0]), "Celeron") !== false) {
                $laptop->setProcessor("Celeron");
            } elseif (strpos(ltrim($specifics[$s]->Value[0]), "Atom") !== false) {
                $laptop->setProcessor("Atom");
            } elseif (strpos(ltrim($specifics[$s]->Value[0]), "AMD Semperon") !== false) {
                $laptop->setProcessor("AMDSemperon");
            } elseif (strpos(ltrim($specifics[$s]->Value[0]), "AMD E-Series") !== false) {
                $laptop->setProcessor("AMDEseries");
            } elseif (strpos(ltrim($specifics[$s]->Value[0]), "Core 2 Duo") !== false) {
                $laptop->setProcessor("Core2Duo");
            } elseif (strpos(ltrim($specifics[$s]->Value[0]), "Core 2 Quad ") !== false) {
                $laptop->setProcessor("Core2Quad ");
            } else {
                $skip = true;
            }

        }
        if (strcmp("Graphics Processing Type", ltrim($specifics[$s]->Name)) === 0) {
            if (strpos($specifics[$s]->Value[0], "Dedicated Graphics") !== false || strpos($specifics[$s]->Value[0], "Dedicated") !== false || strpos($specifics[$s]->Value[0], "dedicated") !== false) {
                $laptop->setGraphics("DedicatedGraphics");
            } elseif (strpos($specifics[$s]->Value[0], "Integrated/On-Board Graphics") !== false) {
                $laptop->setGraphics("Integrated/On-BoardGraphics");
            } elseif (strpos($specifics[$s]->Value[0], "Hybrid Graphicss") !== false) {
                $laptop->setGraphics("HybridGraphics");
            } else {
                $laptop->setGraphics("Integrated/On-BoardGraphics");
            }

        }
        if (strcmp("Memory", ltrim($specifics[$s]->Name)) === 0) {
            $mem = explode("GB", $specifics[$s]->Value[0]);
            $laptop->setMemory($mem[0]);
        }

        if (strcmp("Screen Size", ltrim($specifics[$s]->Name)) === 0) {
            $screenSize = explode("\"", $specifics[$s]->Value[0]);
            $screenSize = explode("in.", $screenSize[0]);
            $laptop->setScreenSize($screenSize[0]);
        }
    }
    $toString = $laptop->ispisARFF();


    if (!$skip && $laptop->isValid()) {
        $uzorak++;
        fwrite($myfileLaptop, $toString);
    }
    unset($a);
    unset($laptop);

}
