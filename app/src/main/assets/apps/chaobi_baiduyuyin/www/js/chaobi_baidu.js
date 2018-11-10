document.addEventListener( "plusready",  function()
{
    var _BARCODE = 'chaobi_baiduyuyin',
		B = window.plus.bridge;
    var chaobi_baiduyuyin =
    {
    	kplbaidu_chaobi_yuyin : function (Argus1, successCallback, errorCallback )
		{
			var success = typeof successCallback !== 'function' ? null : function(args)
			{
				successCallback(args);
			},
			fail = typeof errorCallback !== 'function' ? null : function(code)
			{
				errorCallback(code);
			};
			callbackID = B.callbackId(success, fail);

			return B.exec(_BARCODE, "kplbaidu_chaobi_yuyin", [callbackID, Argus1]);
		},kplbaidu_chaobi_yuyin_ondestory : function (Argus1, successCallback, errorCallback )
          		{
          			var success = typeof successCallback !== 'function' ? null : function(args)
          			{
          				successCallback(args);
          			},
          			fail = typeof errorCallback !== 'function' ? null : function(code)
          			{
          				errorCallback(code);
          			};
          			callbackID = B.callbackId(success, fail);

          			return B.exec(_BARCODE, "kplbaidu_chaobi_yuyin_ondestory", [callbackID, Argus1]);
          		}
    };
    window.plus.chaobi_baiduyuyin = chaobi_baiduyuyin;
}, true );