import React from 'react';
import {Button, Icon, Dropdown, Input, Grid} from 'semantic-ui-react';

class ErrorAccounts extends React.Component {
  logoLetter = (name) => {
    let first = '';
    let second = '';
    let space = false;
    for (let letter = 0; letter < name.length; letter++) {
      if (first.length < 1 && name[letter] !== ' ')
        first = name[letter];
      else if (first.length > 0 && second.length < 1 && name[letter] !== ' ' && space === true)
        second = name[letter];
      else if (name[letter] === ' ')
        space = true;
    }
    if (second !== '')
      return first.toUpperCase() + second.toUpperCase();
    else
      return first.toUpperCase();
  };
  render() {
    const {
      errorAccounts,
      handleErrorAppInfo,
      importErrorAccounts,
      deleteErrorAccount,
      fields
    } = this.props;
    const accounts = errorAccounts.map(item => (
      <div key={item.id} className='account'>
        <Icon name='remove circle' onClick={e => deleteErrorAccount(item.id)}/>
        {(item.logo && item.logo.length > 0) && <img src={item.logo}/>}
        {(!item.logo || item.logo.length < 1) &&
        <div className='logo_letter'>
          <p style={{margin: 'auto'}}>{this.logoLetter(item.name)}</p>
        </div>}
        {Object.keys(fields).map(field => (
          <Input idapp={item.id}
                 key={fields[field]}
                 size='mini'
                 name={fields[field]}
                 value={item[fields[field]]}
                 disabled
                 icon={fields[field] === 'password' && <Icon name='eye' link onClick={e => this.seePassword(item.id)}/>}
                 type={fields[field] === 'password' ? this.state.seePassword[item.id] : 'text'} />
        ))}
        <Input size='mini'
               idapp={item.id}
               name='thirdField'
               error={item.thirdField.value === ''}
               placeholder={item.thirdField.name}
               value={item.thirdField.value}
               onChange={handleErrorAppInfo} />
      </div>
    ));
    return (
      <React.Fragment>
        <p style={{color:'#eb555c',marginBottom: '50px', marginLeft: '70px', marginTop: '10px'}}>Some errors detected for the following account(s):</p>
        <Grid id='accounts'>
          <div className='dropdown_fields'>
            <Dropdown value={fields.field1}/>
            <Dropdown value={fields.field2}/>
            <Dropdown value={fields.field3}/>
            <Dropdown value={fields.field4}/>
            <Dropdown value={'Third field'}/>
          </div>
          {accounts}
        </Grid>
        <Button className={'right'} content='Done!' positive onClick={importErrorAccounts}/>
      </React.Fragment>
    )
  }
}

export default ErrorAccounts;