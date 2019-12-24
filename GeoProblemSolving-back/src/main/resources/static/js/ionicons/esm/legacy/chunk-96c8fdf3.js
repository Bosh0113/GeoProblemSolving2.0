var CACHED_MAP;
var getIconMap = function () {
    if (!CACHED_MAP) {
        var win = window;
        win.Ionicons = win.Ionicons || {};
        CACHED_MAP = win.Ionicons.map = win.Ionicons.map || new Map();
    }
    return CACHED_MAP;
};
var addIcons = function (icons) {
    var map = getIconMap();
    Object.keys(icons).forEach(function (name) {
        map.set(name, icons[name]);
    });
};
var getName = function (name, mode, ios, md) {
    // default to "md" if somehow the mode wasn't set
    mode = (mode || 'md').toLowerCase();
    mode = mode === 'ios' ? 'ios' : 'md';
    // if an icon was passed in using the ios or md attributes
    // set the iconName to whatever was passed in
    if (ios && mode === 'ios') {
        name = ios.toLowerCase();
    }
    else if (md && mode === 'md') {
        name = md.toLowerCase();
    }
    else if (name) {
        name = name.toLowerCase();
        if (!/^md-|^ios-|^logo-/.test(name)) {
            // this does not have one of the defaults
            // so lets auto add in the mode prefix for them
            name = mode + "-" + name;
        }
    }
    if (typeof name !== 'string' || name.trim() === '') {
        return null;
    }
    // only allow alpha characters and dash
    var invalidChars = name.replace(/[a-z]|-|\d/gi, '');
    if (invalidChars !== '') {
        return null;
    }
    return name;
};
var getSrc = function (src) {
    if (typeof src === 'string') {
        src = src.trim();
        if (isSrc(src)) {
            return src;
        }
    }
    return null;
};
var isSrc = function (str) {
    return str.length > 0 && /(\/|\.)/.test(str);
};
var isValid = function (elm) {
    if (elm.nodeType === 1) {
        if (elm.nodeName.toLowerCase() === 'script') {
            return false;
        }
        for (var i = 0; i < elm.attributes.length; i++) {
            var val = elm.attributes[i].value;
            if (typeof val === 'string' && val.toLowerCase().indexOf('on') === 0) {
                return false;
            }
        }
        for (var i = 0; i < elm.childNodes.length; i++) {
            if (!isValid(elm.childNodes[i])) {
                return false;
            }
        }
    }
    return true;
};
export { addIcons as a, getSrc as b, getIconMap as c, isValid as d, getName as g, isSrc as i };
