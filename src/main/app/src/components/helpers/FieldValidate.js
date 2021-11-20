
export const Required = (val) => {
  return val ? undefined : 'Không được bỏ trống'
}

export const NotIn = (val, vals) => {
  return vals.includes(val) ? `Đã tồn tại [${vals.toString()}]` : undefined
}

export const FieldNumberOnly = (val) => {
    return val?.replace(/[^0-9]/g,'');
}

export const Alphabetical = (num) => {
  let s = '', t;

  while (num > 0) {
    t = (num - 1) % 26;
    s = String.fromCharCode(65 + t) + s;
    num = (num - t)/26 | 0;
  }

  console.log(s)
  return s || undefined;
}

export const TruncateSharp = (id) => {
  return id.includes('#') ? id.substring(id.indexOf('#') + 1) : id;
}