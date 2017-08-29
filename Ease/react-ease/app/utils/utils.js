export const passwordRegexp = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\S+$).{8,}$/;
export const emailRegexp = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

export const usernameRegexp = /^[a-z0-9]{4,21}$/;

export function checkTeamUsernameErrors(username){
    var value = {
        error: false,
        message: ''
    };
    if (username.length < 4 || username.length > 21)
        value.message = "Sorry, usernames must be greater than 3 characters and fewer than 22 characters.";
    else if (username.match(usernameRegexp) === null)
        value.message = "Sorry, usernames must contain only lowercase characters.";
    if (value.message.length > 0)
        value.error = true;
    return value;
}

export const jobRoles = [
    'Administrative/Facilities',
    'Accounting/Finance',
    'Business Development',
    'Business Owner',
    'Customer Support',
    'Data/Analytics/Business Intelligence',
    'Design',
    'Engineering (Software)',
    'Marketing',
    'Media/Communications',
    'Operations',
    'Product Management',
    'Program/Project Management',
    'Research',
    'Sales',
    'Other'
];
export const teamUserState = {
    invited: 0,
    registered: 1,
    accepted: 2
};

export const teamUserRoleValues = [
    {key: '1', text: 'member', value:1},
    {key: '2', text: 'admin', value:2},
    {key: '3', text: 'owner', value:3}
];