for(var i = 0; i < 402; i++) { var scriptId = 'u' + i; window[scriptId] = document.getElementById(scriptId); }

$axure.eventManager.pageLoad(
function (e) {

if ((GetGlobalVariableValue('samtycke')) == ('fel')) {

	SetPanelState('u19', 'pd10u19','none','',500,'none','',500);

	SetPanelVisibility('u10','','none',500);

}
else
if ((GetGlobalVariableValue('samtycke')) == ('ja')) {

	SetPanelState('u19', 'pd1u19','none','',500,'none','',500);

	SetPanelVisibility('u10','','none',500);

}
else
if ((GetGlobalVariableValue('samtycke')) != ('ja')) {

	SetPanelState('u19', 'pd0u19','none','',500,'none','',500);

}

});
gv_vAlignTable['u370'] = 'top';gv_vAlignTable['u318'] = 'top';gv_vAlignTable['u299'] = 'top';u202.tabIndex = 0;

u202.style.cursor = 'pointer';
$axure.eventManager.click('u202', function(e) {

if (true) {

	SetPanelState('u19', 'pd2u19','none','',500,'none','',500);

	ScrollToWidget('u401', false,true,'none',500);

}
});
gv_vAlignTable['u202'] = 'top';gv_vAlignTable['u400'] = 'top';gv_vAlignTable['u216'] = 'top';u333.tabIndex = 0;

u333.style.cursor = 'pointer';
$axure.eventManager.click('u333', function(e) {

if (true) {

	SetPanelState('u19', 'pd2u19','none','',500,'none','',500);

}
});
gv_vAlignTable['u333'] = 'top';gv_vAlignTable['u97'] = 'center';gv_vAlignTable['u152'] = 'top';u347.tabIndex = 0;

u347.style.cursor = 'pointer';
$axure.eventManager.click('u347', function(e) {

if (true) {

	SetPanelVisibility('u340','hidden','none',500);

}
});
gv_vAlignTable['u347'] = 'top';gv_vAlignTable['u166'] = 'top';gv_vAlignTable['u298'] = 'center';gv_vAlignTable['u201'] = 'top';gv_vAlignTable['u1'] = 'center';gv_vAlignTable['u215'] = 'top';gv_vAlignTable['u193'] = 'center';document.getElementById('u11_img').tabIndex = 0;

u11.style.cursor = 'pointer';
$axure.eventManager.click('u11', function(e) {

if (true) {

	SetPanelState('u19', 'pd1u19','none','',500,'none','',500);

}
});
gv_vAlignTable['u332'] = 'top';gv_vAlignTable['u131'] = 'top';gv_vAlignTable['u151'] = 'top';gv_vAlignTable['u346'] = 'top';gv_vAlignTable['u388'] = 'top';gv_vAlignTable['u100'] = 'top';gv_vAlignTable['u39'] = 'center';u214.tabIndex = 0;

u214.style.cursor = 'pointer';
$axure.eventManager.click('u214', function(e) {

if (true) {

	SetPanelState('u19', 'pd2u19','none','',500,'none','',500);

}
});
gv_vAlignTable['u67'] = 'center';gv_vAlignTable['u331'] = 'top';
u378.style.cursor = 'pointer';
$axure.eventManager.click('u378', function(e) {

if (true) {

	SetPanelVisibility('u363','hidden','none',500);

	SetPanelState('u19', 'pd0u19','none','',500,'none','',500);

	SetPanelVisibility('u10','hidden','none',500);

}
});
document.getElementById('u17_img').tabIndex = 0;

u17.style.cursor = 'pointer';
$axure.eventManager.click('u17', function(e) {

if (true) {

	SetPanelState('u19', 'pd7u19','none','',500,'none','',500);

}
});

u48.style.cursor = 'pointer';
$axure.eventManager.click('u48', function(e) {

if (true) {

	self.location.href='Om Mina vårdkontakter: http://www.minavardkontakter.se/C125755F00329208/p/OSAL-7PBG4J?opendocument';

}
});
gv_vAlignTable['u345'] = 'top';gv_vAlignTable['u24'] = 'top';gv_vAlignTable['u80'] = 'center';gv_vAlignTable['u85'] = 'top';gv_vAlignTable['u330'] = 'top';
u42.style.cursor = 'pointer';
$axure.eventManager.click('u42', function(e) {

if (true) {

	SetPanelState('u30', 'pd1u30','none','',500,'none','',500);

}
});
gv_vAlignTable['u344'] = 'center';gv_vAlignTable['u41'] = 'top';gv_vAlignTable['u95'] = 'center';document.getElementById('u54_img').tabIndex = 0;

u54.style.cursor = 'pointer';
$axure.eventManager.click('u54', function(e) {

if (true) {

	SetPanelState('u30', 'pd2u30','none','',500,'none','',500);

}
});
gv_vAlignTable['u177'] = 'top';gv_vAlignTable['u37'] = 'top';gv_vAlignTable['u93'] = 'top';gv_vAlignTable['u112'] = 'center';gv_vAlignTable['u307'] = 'top';gv_vAlignTable['u285'] = 'top';gv_vAlignTable['u126'] = 'top';gv_vAlignTable['u50'] = 'top';document.getElementById('u343_img').tabIndex = 0;

u343.style.cursor = 'pointer';
$axure.eventManager.click('u343', function(e) {

if (true) {

	SetPanelVisibility('u340','hidden','none',500);

}
});
gv_vAlignTable['u162'] = 'top';gv_vAlignTable['u357'] = 'top';document.getElementById('u79_img').tabIndex = 0;

u79.style.cursor = 'pointer';
$axure.eventManager.click('u79', function(e) {

if (true) {

	SetPanelState('u19', 'pd2u19','none','',500,'none','',500);

}
});
gv_vAlignTable['u176'] = 'top';gv_vAlignTable['u55'] = 'center';gv_vAlignTable['u149'] = 'top';gv_vAlignTable['u306'] = 'center';gv_vAlignTable['u284'] = 'top';gv_vAlignTable['u12'] = 'center';gv_vAlignTable['u342'] = 'center';gv_vAlignTable['u356'] = 'center';
u372.style.cursor = 'pointer';
$axure.eventManager.click('u372', function(e) {

if (true) {

	SetPanelVisibility('u363','hidden','none',500);

}
});
gv_vAlignTable['u148'] = 'top';gv_vAlignTable['u110'] = 'top';gv_vAlignTable['u61'] = 'top';gv_vAlignTable['u20'] = 'top';gv_vAlignTable['u124'] = 'top';gv_vAlignTable['u279'] = 'top';gv_vAlignTable['u241'] = 'top';gv_vAlignTable['u160'] = 'top';document.getElementById('u297_img').tabIndex = 0;

u297.style.cursor = 'pointer';
$axure.eventManager.click('u297', function(e) {

if (true) {

	SetPanelVisibility('u294','hidden','none',500);

}
});
gv_vAlignTable['u8'] = 'center';gv_vAlignTable['u49'] = 'top';gv_vAlignTable['u25'] = 'top';gv_vAlignTable['u174'] = 'top';gv_vAlignTable['u228'] = 'center';gv_vAlignTable['u374'] = 'center';gv_vAlignTable['u304'] = 'center';gv_vAlignTable['u282'] = 'top';
u76.style.cursor = 'pointer';
$axure.eventManager.click('u76', function(e) {

if (true) {

	self.location.href='http://www.datainspektionen.se/lagar-och-regler/patientdatalagen/';

}
});
gv_vAlignTable['u123'] = 'top';gv_vAlignTable['u278'] = 'top';
u240.style.cursor = 'pointer';
$axure.eventManager.click('u240', function(e) {

if (true) {

	SetPanelVisibility('u294','','none',500);

}
});
gv_vAlignTable['u296'] = 'center';gv_vAlignTable['u33'] = 'top';gv_vAlignTable['u254'] = 'top';gv_vAlignTable['u181'] = 'top';document.getElementById('u94_img').tabIndex = 0;
HookHover('u94', false);

u94.style.cursor = 'pointer';
$axure.eventManager.click('u94', function(e) {

if (true) {

	SetPanelState('u19', 'pd6u19','none','',500,'none','',500);

}
});
gv_vAlignTable['u122'] = 'top';gv_vAlignTable['u358'] = 'top';gv_vAlignTable['u317'] = 'top';gv_vAlignTable['u51'] = 'top';gv_vAlignTable['u290'] = 'center';gv_vAlignTable['u109'] = 'top';gv_vAlignTable['u253'] = 'top';gv_vAlignTable['u172'] = 'top';gv_vAlignTable['u399'] = 'top';gv_vAlignTable['u302'] = 'top';gv_vAlignTable['u121'] = 'top';document.getElementById('u56_img').tabIndex = 0;

u56.style.cursor = 'pointer';
$axure.eventManager.click('u56', function(e) {

if (true) {

	SetPanelState('u30', 'pd1u30','none','',500,'none','',500);

}
});
gv_vAlignTable['u316'] = 'top';gv_vAlignTable['u108'] = 'center';gv_vAlignTable['u252'] = 'top';gv_vAlignTable['u239'] = 'top';gv_vAlignTable['u301'] = 'top';gv_vAlignTable['u120'] = 'top';gv_vAlignTable['u315'] = 'center';u293.tabIndex = 0;

u293.style.cursor = 'pointer';
$axure.eventManager.click('u293', function(e) {

if (true) {

	SetPanelVisibility('u286','hidden','none',500);

}
});
gv_vAlignTable['u293'] = 'top';gv_vAlignTable['u21'] = 'top';gv_vAlignTable['u63'] = 'top';gv_vAlignTable['u251'] = 'top';gv_vAlignTable['u170'] = 'top';gv_vAlignTable['u164'] = 'top';u29.tabIndex = 0;

u29.style.cursor = 'pointer';
$axure.eventManager.click('u29', function(e) {

if (true) {

	NewTab('https://www.minavardkontakter.se/C125755F00329208/p/VGDO-7PSG9V', "");

}
});
gv_vAlignTable['u29'] = 'top';gv_vAlignTable['u82'] = 'top';gv_vAlignTable['u16'] = 'center';gv_vAlignTable['u238'] = 'center';gv_vAlignTable['u200'] = 'top';HookHover('u314', false);
gv_vAlignTable['u292'] = 'top';gv_vAlignTable['u77'] = 'top';gv_vAlignTable['u18'] = 'center';
u32.style.cursor = 'pointer';
$axure.eventManager.click('u32', function(e) {

if (true) {

	self.location.href='http://www.vardguiden.se/Sa-funkar-det/Lagar--rattigheter/Lagar-i-halso--och-sjukvard/Sa-hanteras-dina-personuppgifter/ ';

}
});
gv_vAlignTable['u250'] = 'top';gv_vAlignTable['u387'] = 'center';gv_vAlignTable['u168'] = 'top';gv_vAlignTable['u34'] = 'top';gv_vAlignTable['u90'] = 'top';gv_vAlignTable['u81'] = 'top';gv_vAlignTable['u132'] = 'top';gv_vAlignTable['u368'] = 'center';gv_vAlignTable['u146'] = 'top';document.getElementById('u52_img').tabIndex = 0;

u52.style.cursor = 'pointer';
$axure.eventManager.click('u52', function(e) {

if (true) {

	SetPanelState('u30', 'pd0u30','none','',500,'none','',500);

}
});
gv_vAlignTable['u119'] = 'top';gv_vAlignTable['u369'] = 'top';gv_vAlignTable['u277'] = 'top';gv_vAlignTable['u47'] = 'center';gv_vAlignTable['u212'] = 'center';gv_vAlignTable['u391'] = 'top';
u385.style.cursor = 'pointer';
$axure.eventManager.click('u385', function(e) {

if (true) {

	SetPanelVisibility('u363','hidden','none',500);

}
});
gv_vAlignTable['u226'] = 'center';gv_vAlignTable['u118'] = 'center';gv_vAlignTable['u262'] = 'top';gv_vAlignTable['u89'] = 'center';gv_vAlignTable['u249'] = 'top';gv_vAlignTable['u130'] = 'top';u384.tabIndex = 0;

u384.style.cursor = 'pointer';
$axure.eventManager.click('u384', function(e) {

if (true) {

	SetPanelVisibility('u363','hidden','none',500);

}
});
gv_vAlignTable['u384'] = 'top';gv_vAlignTable['u261'] = 'top';gv_vAlignTable['u275'] = 'top';gv_vAlignTable['u329'] = 'top';gv_vAlignTable['u248'] = 'top';gv_vAlignTable['u210'] = 'top';document.getElementById('u107_img').tabIndex = 0;

u107.style.cursor = 'pointer';
$axure.eventManager.click('u107', function(e) {

if (true) {

	NewTab($axure.globalVariableProvider.getLinkUrl('intyget.html'), "");

}
});
gv_vAlignTable['u383'] = 'top';gv_vAlignTable['u224'] = 'center';u379.tabIndex = 0;

u379.style.cursor = 'pointer';
$axure.eventManager.click('u379', function(e) {

if (true) {

	SetPanelVisibility('u363','hidden','none',500);

}
});
gv_vAlignTable['u379'] = 'top';gv_vAlignTable['u260'] = 'top';
u397.style.cursor = 'pointer';
$axure.eventManager.click('u397', function(e) {

if (true) {

	SetPanelState('u19', 'pd1u19','none','',500,'none','',500);

	SetPanelVisibility('u10','','none',500);

	ScrollToWidget('u401', false,true,'none',500);

	SetPanelVisibility('u363','hidden','none',500);

}
});
gv_vAlignTable['u59'] = 'center';gv_vAlignTable['u189'] = 'top';gv_vAlignTable['u35'] = 'top';gv_vAlignTable['u91'] = 'top';gv_vAlignTable['u328'] = 'center';
u44.style.cursor = 'pointer';
$axure.eventManager.click('u44', function(e) {

if (true) {

	self.location.href='http://www.minavardkontakter.se/C125755F00329208/p/KONT-8ZSGV8?opendocument';

}
});
gv_vAlignTable['u106'] = 'center';gv_vAlignTable['u64'] = 'top';gv_vAlignTable['u382'] = 'top';gv_vAlignTable['u86'] = 'top';u396.tabIndex = 0;

u396.style.cursor = 'pointer';
$axure.eventManager.click('u396', function(e) {

if (true) {

	SetPanelVisibility('u363','hidden','none',500);

}
});
gv_vAlignTable['u396'] = 'top';document.getElementById('u237_img').tabIndex = 0;

u237.style.cursor = 'pointer';
$axure.eventManager.click('u237', function(e) {

if (true) {

	SetPanelState('u19', 'pd3u19','none','',500,'none','',500);

}
});
gv_vAlignTable['u156'] = 'top';gv_vAlignTable['u188'] = 'top';u354.tabIndex = 0;

u354.style.cursor = 'pointer';
$axure.eventManager.click('u354', function(e) {

if (true) {

	SetPanelVisibility('u340','','none',500);

}
});
gv_vAlignTable['u53'] = 'center';gv_vAlignTable['u381'] = 'center';gv_vAlignTable['u222'] = 'center';gv_vAlignTable['u6'] = 'center';gv_vAlignTable['u395'] = 'top';gv_vAlignTable['u236'] = 'center';gv_vAlignTable['u209'] = 'top';gv_vAlignTable['u353'] = 'center';gv_vAlignTable['u104'] = 'center';gv_vAlignTable['u259'] = 'top';gv_vAlignTable['u70'] = 'top';gv_vAlignTable['u394'] = 'top';document.getElementById('u235_img').tabIndex = 0;

u235.style.cursor = 'pointer';
$axure.eventManager.click('u235', function(e) {

if (true) {

	SetPanelState('u19', 'pd4u19','none','',500,'none','',500);

}
});
document.getElementById('u13_img').tabIndex = 0;

u13.style.cursor = 'pointer';
$axure.eventManager.click('u13', function(e) {

if (true) {

	SetPanelState('u19', 'pd8u19','none','',500,'none','',500);

}
});
gv_vAlignTable['u74'] = 'top';gv_vAlignTable['u339'] = 'top';gv_vAlignTable['u158'] = 'top';u220.tabIndex = 0;

u220.style.cursor = 'pointer';
$axure.eventManager.click('u220', function(e) {

if (true) {

	SetPanelState('u19', 'pd2u19','none','',500,'none','',500);

}
});
gv_vAlignTable['u220'] = 'top';gv_vAlignTable['u3'] = 'center';gv_vAlignTable['u393'] = 'center';gv_vAlignTable['u31'] = 'top';gv_vAlignTable['u234'] = 'top';gv_vAlignTable['u351'] = 'center';gv_vAlignTable['u178'] = 'top';gv_vAlignTable['u199'] = 'top';gv_vAlignTable['u365'] = 'center';gv_vAlignTable['u92'] = 'top';u102.tabIndex = 0;

u102.style.cursor = 'pointer';
$axure.eventManager.click('u102', function(e) {

if (true) {

	SetPanelState('u19', 'pd2u19','none','',500,'none','',500);

}
});
gv_vAlignTable['u102'] = 'top';gv_vAlignTable['u338'] = 'top';gv_vAlignTable['u312'] = 'top';gv_vAlignTable['u116'] = 'top';gv_vAlignTable['u191'] = 'center';gv_vAlignTable['u233'] = 'top';gv_vAlignTable['u87'] = 'top';gv_vAlignTable['u308'] = 'top';gv_vAlignTable['u247'] = 'center';gv_vAlignTable['u68'] = 'top';gv_vAlignTable['u28'] = 'top';u101.tabIndex = 0;

u101.style.cursor = 'pointer';
$axure.eventManager.click('u101', function(e) {

if (true) {

	SetPanelState('u19', 'pd2u19','none','',500,'none','',500);

}
});

u115.style.cursor = 'pointer';
$axure.eventManager.click('u115', function(e) {

if (true) {

SetCheckState('u147', true);

SetCheckState('u150', true);

SetCheckState('u153', true);

SetCheckState('u187', true);

SetCheckState('u155', true);

SetCheckState('u167', true);

SetCheckState('u169', true);

SetCheckState('u157', true);

SetCheckState('u185', true);

SetCheckState('u171', true);

SetCheckState('u159', true);

SetCheckState('u183', true);

SetCheckState('u173', true);

SetCheckState('u175', true);

SetCheckState('u163', true);

SetCheckState('u165', true);

}
});

u390.style.cursor = 'pointer';
$axure.eventManager.click('u390', function(e) {

if (true) {

	SetPanelVisibility('u363','hidden','none',500);

}
});
gv_vAlignTable['u313'] = 'top';gv_vAlignTable['u291'] = 'top';gv_vAlignTable['u309'] = 'top';gv_vAlignTable['u62'] = 'top';u219.tabIndex = 0;

u219.style.cursor = 'pointer';
$axure.eventManager.click('u219', function(e) {

if (true) {

}
});
gv_vAlignTable['u219'] = 'top';gv_vAlignTable['u26'] = 'top';u377.tabIndex = 0;

u377.style.cursor = 'pointer';
$axure.eventManager.click('u377', function(e) {

if (true) {

	SetPanelVisibility('u363','hidden','none',500);

}
});
gv_vAlignTable['u377'] = 'top';u114.tabIndex = 0;

u114.style.cursor = 'pointer';
$axure.eventManager.click('u114', function(e) {

if (true) {

	SetPanelState('u19', 'pd2u19','none','',500,'none','',500);

}
});
gv_vAlignTable['u57'] = 'center';gv_vAlignTable['u326'] = 'top';gv_vAlignTable['u245'] = 'center';gv_vAlignTable['u14'] = 'center';u218.tabIndex = 0;

u218.style.cursor = 'pointer';
$axure.eventManager.click('u218', function(e) {

if (true) {

	SetPanelVisibility('u363','','none',500);

	SetPanelState('u366', 'pd2u366','none','',500,'none','',500);

}
});
gv_vAlignTable['u218'] = 'top';gv_vAlignTable['u362'] = 'top';u389.tabIndex = 0;

u389.style.cursor = 'pointer';
$axure.eventManager.click('u389', function(e) {

if (true) {

	SetPanelVisibility('u363','hidden','none',500);

}
});
gv_vAlignTable['u389'] = 'top';document.getElementById('u38_img').tabIndex = 0;

u38.style.cursor = 'pointer';
$axure.eventManager.click('u38', function(e) {

if (true) {

	SetPanelVisibility('u363','','none',500);

	SetPanelState('u366', 'pd3u366','none','',500,'none','',500);

}
});
gv_vAlignTable['u376'] = 'top';gv_vAlignTable['u99'] = 'top';gv_vAlignTable['u349'] = 'top';gv_vAlignTable['u311'] = 'top';u230.tabIndex = 0;

u230.style.cursor = 'pointer';
$axure.eventManager.click('u230', function(e) {

if (true) {

	SetPanelState('u19', 'pd1u19','none','',500,'none','',500);

}
});
gv_vAlignTable['u127'] = 'top';gv_vAlignTable['u186'] = 'top';gv_vAlignTable['u325'] = 'center';gv_vAlignTable['u361'] = 'top';gv_vAlignTable['u375'] = 'top';gv_vAlignTable['u27'] = 'top';u348.tabIndex = 0;

u348.style.cursor = 'pointer';
$axure.eventManager.click('u348', function(e) {

if (true) {

	SetPanelVisibility('u340','','none',500);

}
});
gv_vAlignTable['u310'] = 'top';gv_vAlignTable['u207'] = 'top';gv_vAlignTable['u40'] = 'top';u243.tabIndex = 0;

u243.style.cursor = 'pointer';
$axure.eventManager.click('u243', function(e) {

if (true) {

	SetPanelVisibility('u286','','none',500);

}
});
gv_vAlignTable['u360'] = 'center';gv_vAlignTable['u257'] = 'top';gv_vAlignTable['u69'] = 'top';document.getElementById('u289_img').tabIndex = 0;

u289.style.cursor = 'pointer';
$axure.eventManager.click('u289', function(e) {

if (true) {

	SetPanelVisibility('u286','hidden','none',500);

	SetPanelState('u19', 'pd7u19','none','',500,'none','',500);

}
});

u45.style.cursor = 'pointer';
$axure.eventManager.click('u45', function(e) {

if (true) {

	self.location.href='http://www.forsakringskassan.se/privatpers/om_sjalvbetjaning/!ut/p/b1/hY7BCoJAGISfpRfYf_5Vd9ejW5AWSlmQ7SUsQoRWO0TR26fRtZrbwDcfQ44qcl19b5v61vZdfRm7UwcLILacYF4ajSyY5tKUkQR4APYDgC9J8G-_INcevXicvIDgUMoAOgw4VrEGK9qRexuMRG5Hg0kUI-N0y-vClrNl9AF-PCjS3p_p6qvnZtVMXingJIY!/dl4/d5/L2dJQSEvUUt3QS80SmtFL1o2XzgyME1CQjFBMDhTMDgwSTlIOUVGOTJHQTMx/ ';

}
});
document.getElementById('u203_img').tabIndex = 0;

u203.style.cursor = 'pointer';
$axure.eventManager.click('u203', function(e) {

if (true) {

	SetPanelState('u19', 'pd5u19','none','',500,'none','',500);

}
});
gv_vAlignTable['u184'] = 'top';gv_vAlignTable['u323'] = 'center';gv_vAlignTable['u242'] = 'top';
u43.style.cursor = 'pointer';
$axure.eventManager.click('u43', function(e) {

if (true) {

	self.location.href='http://www.minavardkontakter.se/C125755F00329208/p/startpage?opendocument';

}
});
gv_vAlignTable['u337'] = 'center';gv_vAlignTable['u256'] = 'top';
u22.style.cursor = 'pointer';
$axure.eventManager.click('u22', function(e) {

if (true) {

	SetPanelState('u19', 'pd1u19','none','',500,'none','',500);

}
});
gv_vAlignTable['u288'] = 'center';gv_vAlignTable['u129'] = 'top';gv_vAlignTable['u71'] = 'top';gv_vAlignTable['u133'] = 'top';gv_vAlignTable['u205'] = 'top';gv_vAlignTable['u197'] = 'center';document.getElementById('u336_img').tabIndex = 0;
HookHover('u336', false);

u336.style.cursor = 'pointer';
$axure.eventManager.click('u336', function(e) {

if (true) {

	SetPanelState('u19', 'pd2u19','none','',500,'none','',500);

}
});
gv_vAlignTable['u255'] = 'top';document.getElementById('u15_img').tabIndex = 0;

u15.style.cursor = 'pointer';
$axure.eventManager.click('u15', function(e) {

if (true) {

	SetPanelState('u19', 'pd9u19','none','',500,'none','',500);

}
});
gv_vAlignTable['u128'] = 'top';gv_vAlignTable['u60'] = 'top';gv_vAlignTable['u75'] = 'top';gv_vAlignTable['u204'] = 'center';document.getElementById('u359_img').tabIndex = 0;
HookHover('u359', false);

u359.style.cursor = 'pointer';
$axure.eventManager.click('u359', function(e) {

if (true) {

	SetPanelVisibility('u363','','none',500);

	SetPanelState('u366', 'pd0u366','none','',500,'none','',500);

}
});
u321.tabIndex = 0;

u321.style.cursor = 'pointer';
$axure.eventManager.click('u321', function(e) {

if (true) {

	SetPanelVisibility('u363','','none',500);

	SetPanelState('u366', 'pd4u366','none','',500,'none','',500);

}
});
gv_vAlignTable['u335'] = 'top';gv_vAlignTable['u23'] = 'top';gv_vAlignTable['u154'] = 'top';u371.tabIndex = 0;

u371.style.cursor = 'pointer';
$axure.eventManager.click('u371', function(e) {

if (true) {

	SetPanelVisibility('u363','hidden','none',500);

}
});
gv_vAlignTable['u371'] = 'top';gv_vAlignTable['u125'] = 'top';gv_vAlignTable['u232'] = 'top';gv_vAlignTable['u84'] = 'center';gv_vAlignTable['u258'] = 'top';gv_vAlignTable['u320'] = 'center';gv_vAlignTable['u4'] = 'top';
u217.style.cursor = 'pointer';
$axure.eventManager.click('u217', function(e) {

if (true) {

	SetPanelState('u19', 'pd4u19','none','',500,'none','',500);

}
});
gv_vAlignTable['u195'] = 'center';gv_vAlignTable['u65'] = 'top';gv_vAlignTable['u73'] = 'center';gv_vAlignTable['u334'] = 'top';