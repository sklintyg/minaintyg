for(var i = 0; i < 9; i++) { var scriptId = 'u' + i; window[scriptId] = document.getElementById(scriptId); }

$axure.eventManager.pageLoad(
function (e) {

});
gv_vAlignTable['u4'] = 'top';document.getElementById('u5_img').tabIndex = 0;

u5.style.cursor = 'pointer';
$axure.eventManager.click('u5', function(e) {

if (true) {

SetGlobalVariableValue('samtycke', 'ja');

	self.location.href=$axure.globalVariableProvider.getLinkUrl('Mina_intyg.html');

}
});
gv_vAlignTable['u6'] = 'center';document.getElementById('u7_img').tabIndex = 0;

u7.style.cursor = 'pointer';
$axure.eventManager.click('u7', function(e) {

if (true) {

SetGlobalVariableValue('samtycke', 'fel');

	self.location.href=$axure.globalVariableProvider.getLinkUrl('Mina_intyg.html');

}
});
gv_vAlignTable['u8'] = 'center';gv_vAlignTable['u0'] = 'top';document.getElementById('u1_img').tabIndex = 0;

u1.style.cursor = 'pointer';
$axure.eventManager.click('u1', function(e) {

if (true) {

SetGlobalVariableValue('samtycke', 'nej');

	self.location.href=$axure.globalVariableProvider.getLinkUrl('Mina_intyg.html');

}
});
gv_vAlignTable['u2'] = 'center';gv_vAlignTable['u3'] = 'top';